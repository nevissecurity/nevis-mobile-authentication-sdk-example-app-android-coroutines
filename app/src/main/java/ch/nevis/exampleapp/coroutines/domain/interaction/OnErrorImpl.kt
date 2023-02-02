/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.common.error.ErrorHandlerChain
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.error.MobileAuthenticationClientException
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.OperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.MobileAuthenticationClientError
import ch.nevis.mobile.sdk.api.util.Consumer
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Generic implementation of a [Consumer] that accepts a [MobileAuthenticationClientError].
 * This implementation first checks if there is an active cancellableContinuation in the operation
 * state and if yes it will be resumed with an [ErrorResponse] object. If the cancellableContinuation
 * is null or not active the accepted error will be delegated to the [ErrorHandlerChain] directly.
 */
class OnErrorImpl<S : OperationState, E : MobileAuthenticationClientError>(
    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<S>,

    /**
     * An [ErrorHandlerChain] implementation.
     */
    private val errorHandlerChain: ErrorHandlerChain
) : Consumer<E> {

    //region Consumer
    override fun accept(error: E) {
        val operationState = stateRepository.get() ?: throw BusinessException.invalidState()
        val operation = operationState.operation
        val exception = MobileAuthenticationClientException(
            operation,
            error
        )

        Timber.asTree().sdk(
            String.format(
                "Error occurred during operation %s: %s",
                operation.name,
                error.description()
            )
        )

        var foundCancellableContinuationToResume = false
        operationState.cancellableContinuation?.let {
            if (it.isActive) {
                it.resume(ErrorResponse(exception))
                foundCancellableContinuationToResume = true
            }
        }

        operationState.reset()
        stateRepository.reset()

        if (!foundCancellableContinuationToResume) {
            errorHandlerChain.handle(
                exception
            )
        }
    }
    //endregion
}