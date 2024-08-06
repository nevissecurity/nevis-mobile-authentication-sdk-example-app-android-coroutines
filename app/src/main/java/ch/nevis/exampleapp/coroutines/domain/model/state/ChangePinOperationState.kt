/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.state

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.pin.PinChangeHandler
import kotlinx.coroutines.CancellableContinuation

/**
 * [OperationState] implementation for change PIN operation.
 *
 * @constructor Creates a new instance.
 * @param cancellableContinuation A [CancellableContinuation] instance of a `suspendCancellableCoroutine`
 *  Kotlin coroutine block. The [CancellableContinuation] will be resumed when the SDK finished an
 *  operation step.
 */
data class ChangePinOperationState(
    override var cancellableContinuation: CancellableContinuation<Response>? = null
) : OperationState() {

    //region Properties
    /**
     * Overridden operation property.
     */
    override val operation: Operation = Operation.CHANGE_PIN

    /**
     * [PinChangeHandler] object received during change PIN operation.
     */
    var pinChangeHandler: PinChangeHandler? = null
    //endregion

    //region OperationState
    override fun reset() {
        pinChangeHandler = null
        cancellableContinuation = null
    }
    //endregion
}
