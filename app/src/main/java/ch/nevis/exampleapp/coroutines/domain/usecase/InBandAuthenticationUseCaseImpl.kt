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
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerifier
import ch.nevis.mobile.sdk.api.util.Consumer
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [InBandAuthenticationUseCase] interface.
 */
class InBandAuthenticationUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider,

    /**
     * An instance of a [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>,

    /**
     * An instance of a [AuthenticatorSelector] implementation.
     */
    private val authenticatorSelector: AuthenticatorSelector,

    /**
     * An instance of a [PinUserVerifier] implementation.
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
     * An instance of a [Consumer] implementation that accepts a [AuthorizationProvider] object on
     * successful authentication.
     */
    private val onSuccess: Consumer<AuthorizationProvider>,

    /**
     * An instance of a [Consumer] implementation that accepts a [AuthenticationError] object.
     */
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