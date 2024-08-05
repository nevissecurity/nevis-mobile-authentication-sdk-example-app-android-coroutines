/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePasswordOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [ChangePasswordUseCase] interface.
 */
class ChangePasswordUseCaseImpl(
    /**
     * An instance of an [OperationStateRepository] implementation that may hold an [ChangePasswordOperationState].
     */
    private val stateRepository: OperationStateRepository<ChangePasswordOperationState>
) : ChangePasswordUseCase {

    //region ChangePasswordUseCase
    override suspend fun execute(oldPassword: CharArray, newPassword: CharArray): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState = stateRepository.get() ?: throw BusinessException.invalidState()
            val passwordChangeHandler =
                operationState.passwordChangeHandler ?: throw BusinessException.invalidState()
            operationState.cancellableContinuation = cancellableContinuation
            operationState.passwordChangeHandler = null
            passwordChangeHandler.passwords(oldPassword, newPassword)
        }
    }
    //endregion
}
