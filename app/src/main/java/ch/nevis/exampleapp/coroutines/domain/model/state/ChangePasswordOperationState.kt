/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.state

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.password.PasswordChangeHandler
import kotlinx.coroutines.CancellableContinuation

/**
 * [OperationState] implementation for change Password operation.
 *
 * @constructor Creates a new instance.
 * @param cancellableContinuation A [CancellableContinuation] instance of a `suspendCancellableCoroutine`
 *  Kotlin coroutine block.
 */
data class ChangePasswordOperationState(
    override var cancellableContinuation: CancellableContinuation<Response>? = null
) : OperationState() {

    //region Properties
    /**
     * Overridden operation property.
     */
    override val operation: Operation = Operation.CHANGE_PASSWORD

    /**
     * [PasswordChangeHandler] object received during change Password operation.
     */
    var passwordChangeHandler: PasswordChangeHandler? = null
    //endregion

    //region OperationState
    override fun reset() {
        passwordChangeHandler = null
        cancellableContinuation = null
    }
    //endregion
}
