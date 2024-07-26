/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.error.MobileAuthenticationClientException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.DeviceInformationResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider
import ch.nevis.mobile.sdk.api.localdata.DeviceInformation
import ch.nevis.mobile.sdk.api.operation.OperationError
import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandAuthentication
import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload
import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandRegistration
import ch.nevis.mobile.sdk.api.operation.pin.PinEnroller
import ch.nevis.mobile.sdk.api.operation.selection.AccountSelector
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerifier
import ch.nevis.mobile.sdk.api.util.Consumer
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Default implementation of [ProcessOutOfBandPayloadUseCase] interface.
 *
 * After the [OutOfBandPayload] is process successfully, based on the result an [OutOfBandRegistration]
 * or [OutOfBandAuthentication] operation is started automatically.
 */
class ProcessOutOfBandPayloadUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider,

    /**
     * An instance of a [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>,

    /**
     * An instance of a [CreateDeviceInformationUseCase] implementation.
     */
    private val createDeviceInformationUseCase: CreateDeviceInformationUseCase,

    /**
     * An instance of an [AccountSelector] implementation.
     */
    private val accountSelector: AccountSelector,

    /**
     * An instance of an [AuthenticatorSelector] implementation for registration cases.
     */
    private val registrationAuthenticatorSelector: AuthenticatorSelector,

    /**
     * An instance of an [AuthenticatorSelector] implementation for authentication cases.
     */
    private val authenticationAuthenticatorSelector: AuthenticatorSelector,

    /**
     * An instance of a [PinEnroller] implementation for registration cases.
     */
    private val pinEnroller: PinEnroller,

    /**
     * An instance of a [PinUserVerifier] implementation for authentication cases.
     */
    private val pinUserVerifier: PinUserVerifier,

    /**
     * An instance of a [FingerprintUserVerifier] implementation.
     */
    private val fingerprintUserVerifier: FingerprintUserVerifier,

    /**
     * An instance of a [BiometricUserVerifier] implementation.
     */
    private val biometricUserVerifier: BiometricUserVerifier,

    /**
     * An instance of a [DevicePasscodeUserVerifier] implementation.
     */
    private val devicePasscodeUserVerifier: DevicePasscodeUserVerifier,

    /**
     * An instance of a [Consumer] implementation that accepts a [AuthorizationProvider] object for
     * successful authentication cases.
     */
    private val onAuthenticationSuccess: Consumer<AuthorizationProvider>,

    /**
     * An instance of a [Runnable] implementation for successful registration cases.
     */
    private val onRegistrationSuccess: Runnable,

    /**
     * An instance of a [Consumer] implementation that accepts a [OperationError] object.
     */
    private val onError: Consumer<OperationError>
) : ProcessOutOfBandPayloadUseCase {

    //region Private Classes
    /**
     * Private, inner [Response] class to indicate that based on the [OutOfBandPayload] a out-of-band
     * authentication must be started.
     */
    private data class OutOfBandAuthenticationResponse(
        val authentication: OutOfBandAuthentication
    ) : Response

    /**
     * Private, inner [Response] class to indicate that based on the [OutOfBandPayload] a out-of-band
     * registration must be started.
     */
    private data class OutOfBandRegistrationResponse(
        val registration: OutOfBandRegistration
    ) : Response
    //endregion

    //region ProcessOutOfBandPayloadUseCase
    override suspend fun execute(payload: OutOfBandPayload): Response {
        val response: Response = suspendCancellableCoroutine { cancellableContinuation ->
            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            client.operations().outOfBandOperation()
                .payload(payload)
                .onAuthentication {
                    cancellableContinuation.resume(OutOfBandAuthenticationResponse(it))
                }
                .onRegistration {
                    cancellableContinuation.resume(OutOfBandRegistrationResponse(it))
                }
                .onError {
                    cancellableContinuation.resume(
                        ErrorResponse(
                            MobileAuthenticationClientException(
                                Operation.PROCESS_OUT_OF_BAND_PAYLOAD,
                                it
                            )
                        )
                    )
                }
                .execute()
        }

        return when (response) {
            is ErrorResponse -> response
            is OutOfBandAuthenticationResponse -> authenticate(response.authentication)
            is OutOfBandRegistrationResponse -> {
                val createDeviceInformationUseCaseResponse =
                    createDeviceInformationUseCase.execute()
                if (createDeviceInformationUseCaseResponse is DeviceInformationResponse) {
                    register(
                        response.registration,
                        createDeviceInformationUseCaseResponse.deviceInformation
                    )
                } else {
                    createDeviceInformationUseCaseResponse
                }
            }

            else -> throw BusinessException.invalidState()
        }
    }
    //endregion

    //region Private Interface
    /**
     * Sets-up and executes the given out-of-band authentication.
     *
     * @param outOfBandAuthentication The [OutOfBandAuthentication] operation to be executed.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    private suspend fun authenticate(outOfBandAuthentication: OutOfBandAuthentication): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState =
                UserInteractionOperationState(Operation.OUT_OF_BAND_AUTHENTICATION)
            operationState.cancellableContinuation = cancellableContinuation
            stateRepository.save(operationState)

            outOfBandAuthentication
                .accountSelector(accountSelector)
                .authenticatorSelector(authenticationAuthenticatorSelector)
                .pinUserVerifier(pinUserVerifier)
                .fingerprintUserVerifier(fingerprintUserVerifier)
                .biometricUserVerifier(biometricUserVerifier)
                .devicePasscodeUserVerifier(devicePasscodeUserVerifier)
                .onSuccess(onAuthenticationSuccess)
                .onError(onError)
                .execute()
        }
    }

    /**
     * Sets-up and executes the given out-of-band registration.
     *
     * @param outOfBandRegistration The [OutOfBandRegistration] operation to be executed.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    private suspend fun register(
        outOfBandRegistration: OutOfBandRegistration,
        deviceInformation: DeviceInformation
    ): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState =
                UserInteractionOperationState(Operation.OUT_OF_BAND_REGISTRATION)
            operationState.cancellableContinuation = cancellableContinuation
            stateRepository.save(operationState)

            outOfBandRegistration
                .deviceInformation(deviceInformation)
                .authenticatorSelector(registrationAuthenticatorSelector)
                .pinEnroller(pinEnroller)
                .fingerprintUserVerifier(fingerprintUserVerifier)
                .biometricUserVerifier(biometricUserVerifier)
                .devicePasscodeUserVerifier(devicePasscodeUserVerifier)
                .onSuccess(onRegistrationSuccess)
                .onError(onError)
                .execute()
        }
    }
    //endregion
}
