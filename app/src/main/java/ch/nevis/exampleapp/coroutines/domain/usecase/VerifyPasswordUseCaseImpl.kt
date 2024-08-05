/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [VerifyPasswordUseCase] interface.
 */
class VerifyPasswordUseCaseImpl(
    /**
     * An instance of an [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : VerifyPasswordUseCase {

    //region VerifyPasswordUseCase
    override suspend fun execute(password: CharArray): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState =
                stateRepository.get() ?: throw BusinessException.invalidState()
            val passwordUserVerificationHandler =
                operationState.passwordUserVerificationHandler ?: throw BusinessException.invalidState()
            operationState.cancellableContinuation = cancellableContinuation
            operationState.passwordUserVerificationHandler = null
            passwordUserVerificationHandler.verifyPassword(password)
        }
    }
    //endregion
}
