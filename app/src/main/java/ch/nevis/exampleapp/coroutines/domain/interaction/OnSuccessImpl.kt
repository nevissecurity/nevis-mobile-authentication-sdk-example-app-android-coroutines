/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.CompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.OperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import kotlinx.coroutines.Runnable
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Generic implementation of [Runnable] interface. It resumes the cancellableContinuation of the current
 * operation state with a [CompletedResponse] object and cleans-up the operation state.
 */
class OnSuccessImpl<T : OperationState>(
    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<T>
) : Runnable {

    //region Runnable
    override fun run() {
        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        Timber.asTree().sdk(
            String.format(
                "Operation %s successfully completed.",
                operationState.operation.name
            )
        )

        operationState.reset()
        stateRepository.reset()

        cancellableContinuation.resume(
            CompletedResponse(operationState.operation)
        )
    }
    //endregion
}