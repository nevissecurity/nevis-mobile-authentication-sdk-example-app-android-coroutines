/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePinOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [ChangePinUseCase] interface.
 */
class ChangePinUseCaseImpl(
    /**
     * An instance of an [OperationStateRepository] implementation that may hold an [ChangePinOperationState].
     */
    private val stateRepository: OperationStateRepository<ChangePinOperationState>
) : ChangePinUseCase {

    //region ChangePinUseCase
    override suspend fun execute(oldPin: CharArray, newPin: CharArray): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState = stateRepository.get() ?: throw BusinessException.invalidState()
            val pinChangeHandler =
                operationState.pinChangeHandler ?: throw BusinessException.invalidState()
            operationState.cancellableContinuation = cancellableContinuation
            operationState.pinChangeHandler = null
            pinChangeHandler.pins(oldPin, newPin)
        }
    }
    //endregion
}
