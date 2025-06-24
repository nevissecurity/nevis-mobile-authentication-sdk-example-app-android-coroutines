/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider
import ch.nevis.mobile.sdk.api.operation.AuthenticationError
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.PasswordUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerifier
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.function.Consumer

/**
 * Default implementation of [InBandAuthenticationUseCase] interface.
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of [ClientProvider] interface implementation.
 * @param stateRepository n instance of an [OperationStateRepository] implementation that may hold
 *  a [UserInteractionOperationState].
 * @param authenticatorSelector An instance of [AuthenticatorSelector] interface implementation.
 * @param pinUserVerifier An instance of [PinUserVerifier] interface implementation.
 * @param passwordUserVerifier An instance of [PasswordUserVerifier] interface implementation.
 * @param fingerprintUserVerifier An instance of [FingerprintUserVerifier] interface implementation.
 * @param biometricUserVerifier An instance of [BiometricUserVerifier] interface implementation.
 * @param devicePasscodeUserVerifier An instance of [DevicePasscodeUserVerifier] interface implementation.
 * @param onSuccess An instance of a [Consumer] implementation that accepts a [AuthorizationProvider]
 *  object on successful authentication.
 * @param onError An instance of a [Consumer] implementation that accepts an [AuthenticationError] object.
 */
class InBandAuthenticationUseCaseImpl(
    private val clientProvider: ClientProvider,
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>,
    private val authenticatorSelector: AuthenticatorSelector,
    private val pinUserVerifier: PinUserVerifier,
    private val passwordUserVerifier: PasswordUserVerifier,
    private val fingerprintUserVerifier: FingerprintUserVerifier,
    private val biometricUserVerifier: BiometricUserVerifier,
    private val devicePasscodeUserVerifier: DevicePasscodeUserVerifier,
    private val onSuccess: Consumer<AuthorizationProvider>,
    private val onError: Consumer<AuthenticationError>
) : InBandAuthenticationUseCase {

    //region InBandAuthenticationUseCase
    override suspend fun execute(username: String): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState = UserInteractionOperationState(Operation.AUTHENTICATION)
            operationState.cancellableContinuation = cancellableContinuation
            operationState.username = username
            stateRepository.save(operationState)

            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            client.operations().authentication()
                .username(username)
                .authenticatorSelector(authenticatorSelector)
                .pinUserVerifier(pinUserVerifier)
                .passwordUserVerifier(passwordUserVerifier)
                .fingerprintUserVerifier(fingerprintUserVerifier)
                .biometricUserVerifier(biometricUserVerifier)
                .devicePasscodeUserVerifier(devicePasscodeUserVerifier)
                .onSuccess(onSuccess)
                .onError(onError)
                .execute()
        }
    }
    //endregion
}
