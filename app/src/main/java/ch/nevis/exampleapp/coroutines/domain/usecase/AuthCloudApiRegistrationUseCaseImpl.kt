/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.DeviceInformationResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.mobile.sdk.api.operation.authcloudapi.AuthCloudApiError
import ch.nevis.mobile.sdk.api.operation.pin.PinEnroller
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerifier
import ch.nevis.mobile.sdk.api.util.Consumer
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [AuthCloudApiRegistrationUseCase] interface.
 */
class AuthCloudApiRegistrationUseCaseImpl(
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
     * An instance of an [AuthenticatorSelector] implementation.
     */
    private val authenticatorSelector: AuthenticatorSelector,

    /**
     * An instance of a [PinEnroller] implementation.
     */
    private val pinEnroller: PinEnroller,

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
     * An instance of a [Runnable] implementation.
     */
    private val onSuccess: Runnable,

    /**
     * An instance of a [Consumer] implementation that accepts a [AuthCloudApiError] object.
     */
    private val onError: Consumer<AuthCloudApiError>
) : AuthCloudApiRegistrationUseCase {

    //region AuthCloudApiRegistrationUseCase
    override suspend fun execute(enrollResponse: String?, appLinkUri: String?): Response {
        val createDeviceInformationUseCaseResponse = createDeviceInformationUseCase.execute()
        return if (createDeviceInformationUseCaseResponse is DeviceInformationResponse) {
            suspendCancellableCoroutine { cancellableContinuation ->
                val client =
                    clientProvider.get() ?: throw BusinessException.clientNotInitialized()

                val operationState = UserInteractionOperationState(Operation.REGISTRATION)
                operationState.cancellableContinuation = cancellableContinuation
                stateRepository.save(operationState)

                val operation = client.operations().authCloudApiRegistration()
                    .authenticatorSelector(authenticatorSelector)
                    .deviceInformation(createDeviceInformationUseCaseResponse.deviceInformation)
                    .pinEnroller(pinEnroller)
                    .fingerprintUserVerifier(fingerprintUserVerifier)
                    .biometricUserVerifier(biometricUserVerifier)
                    .devicePasscodeUserVerifier(devicePasscodeUserVerifier)
                    .onSuccess(onSuccess)
                    .onError(onError)

                appLinkUri?.let {
                    operation.appLinkUri(it)
                }

                enrollResponse?.let {
                    operation.enrollResponse(it)
                }

                operation.execute()
            }
        } else {
            createDeviceInformationUseCaseResponse
        }
    }
    //endregion
}
