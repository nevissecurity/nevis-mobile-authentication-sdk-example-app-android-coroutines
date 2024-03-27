/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.CancelledResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePinOperationState
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Default implementation of [CancelOperationUseCase] interface that discovers all known
 * [OperationStateRepository] and tries to find cancel the running operation.
 */
class CancelOperationUseCaseImpl(
    /**
     * An instance of a [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val userInteractionOperationStateRepository: OperationStateRepository<UserInteractionOperationState>,

    /**
     * An instance of a [OperationStateRepository] implementation that may hold an [ChangePinOperationState].
     */
    private val changePinOperationStateRepository: OperationStateRepository<ChangePinOperationState>
) : CancelOperationUseCase {

    //region CancelOperationUseCase
    override suspend fun execute(): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            var cancelled = false
            changePinOperationStateRepository.get()?.let {
                it.cancellableContinuation = cancellableContinuation
                it.pinChangeHandler?.cancel()
                cancelled = true
            }

            userInteractionOperationStateRepository.get()?.let {
                it.cancellableContinuation = cancellableContinuation
                it.accountSelectionHandler?.cancel()
                it.authenticatorSelectionHandler?.cancel()
                it.pinEnrollmentHandler?.cancel()
                it.pinUserVerificationHandler?.cancel()
                it.fingerprintUserVerificationHandler?.cancel()
                it.biometricUserVerificationHandler?.cancel()
                it.osAuthenticationListenHandler?.cancelAuthentication()
                cancelled = true
            }

            if (!cancelled) {
                cancellableContinuation.resume(CancelledResponse())
            }
        }
    }
    //endregion
}