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
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider
import ch.nevis.mobile.sdk.api.operation.OperationError
import ch.nevis.mobile.sdk.api.operation.password.PasswordEnroller
import ch.nevis.mobile.sdk.api.operation.pin.PinEnroller
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerifier
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.function.Consumer

/**
 * Default implementation of [InBandRegistrationUseCase] interface.
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of [ClientProvider] interface implementation.
 * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
 *  a [UserInteractionOperationState].
 * @param createDeviceInformationUseCase An instance of [CreateDeviceInformationUseCase] interface implementation.
 * @param authenticatorSelector An instance of [AuthenticatorSelector] interface implementation
 *  for Registration operation.
 * @param pinEnroller An instance of [PinEnroller] interface implementation.
 * @param passwordEnroller An instance of [PasswordEnroller] interface implementation.
 * @param fingerprintUserVerifier An instance of [FingerprintUserVerifier] interface implementation.
 * @param biometricUserVerifier An instance of [BiometricUserVerifier] interface implementation.
 * @param devicePasscodeUserVerifier An instance of [DevicePasscodeUserVerifier] interface implementation.
 * @param onSuccess An instance of a [Runnable] implementation.
 * @param onError An instance of a [Consumer] implementation that accepts an [OperationError] object.
 */
class InBandRegistrationUseCaseImpl(
    private val clientProvider: ClientProvider,
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>,
    private val createDeviceInformationUseCase: CreateDeviceInformationUseCase,
    private val authenticatorSelector: AuthenticatorSelector,
    private val pinEnroller: PinEnroller,
    private val passwordEnroller: PasswordEnroller,
    private val fingerprintUserVerifier: FingerprintUserVerifier,
    private val biometricUserVerifier: BiometricUserVerifier,
    private val devicePasscodeUserVerifier: DevicePasscodeUserVerifier,
    private val onSuccess: Runnable,
    private val onError: Consumer<OperationError>
) : InBandRegistrationUseCase {

    //region InBandRegistrationUseCase
    override suspend fun execute(
        extId: String,
        authorizationProvider: AuthorizationProvider?
    ): Response {
        val createDeviceInformationUseCaseResponse = createDeviceInformationUseCase.execute()
        return if (createDeviceInformationUseCaseResponse is DeviceInformationResponse) {
            suspendCancellableCoroutine { cancellableContinuation ->
                val client =
                    clientProvider.get() ?: throw BusinessException.clientNotInitialized()

                val operationState = UserInteractionOperationState(Operation.REGISTRATION)
                operationState.cancellableContinuation = cancellableContinuation
                stateRepository.save(operationState)

                val operation = client.operations().registration()
                    .username(extId)
                    .authenticatorSelector(authenticatorSelector)
                    .pinEnroller(pinEnroller)
                    .passwordEnroller(passwordEnroller)
                    .fingerprintUserVerifier(fingerprintUserVerifier)
                    .biometricUserVerifier(biometricUserVerifier)
                    .devicePasscodeUserVerifier(devicePasscodeUserVerifier)
                    .deviceInformation(createDeviceInformationUseCaseResponse.deviceInformation)
                    .onSuccess(onSuccess)
                    .onError(onError)

                authorizationProvider?.let {
                    operation.authorizationProvider(it)
                }
                operation.execute()
            }
        } else {
            createDeviceInformationUseCaseResponse
        }
    }
    //endregion
}
