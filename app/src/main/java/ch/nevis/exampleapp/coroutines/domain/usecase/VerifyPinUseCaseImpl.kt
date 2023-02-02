/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [VerifyPinUseCase] interface.
 */
class VerifyPinUseCaseImpl(
    /**
     * An instance of a [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : VerifyPinUseCase {

    //region VerifyPinUseCase
    override suspend fun execute(pin: CharArray): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState =
                stateRepository.get() ?: throw BusinessException.invalidState()
            val pinUserVerificationHandler =
                operationState.pinUserVerificationHandler ?: throw BusinessException.invalidState()
            operationState.cancellableContinuation = cancellableContinuation
            operationState.pinUserVerificationHandler = null
            pinUserVerificationHandler.verifyPin(pin)
        }
    }
    //endregion
}