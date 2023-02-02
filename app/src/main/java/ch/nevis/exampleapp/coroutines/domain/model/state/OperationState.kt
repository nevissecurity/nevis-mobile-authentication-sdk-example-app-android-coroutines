/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.state

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import kotlinx.coroutines.CancellableContinuation

/**
 * Abstract class that represents a base operation state.
 */
abstract class OperationState {

    /**
     * The [Operation] the state relates to.
     */
    abstract val operation: Operation

    /**
     * A [CancellableContinuation] instance of a `suspendCancellableCoroutine` Kotlin coroutine block.
     * The [CancellableContinuation] will be resumed when the SDK finished an operation step.
     */
    abstract var cancellableContinuation: CancellableContinuation<Response>?

    /**
     * Resets the operation state.
     */
    abstract fun reset()
}