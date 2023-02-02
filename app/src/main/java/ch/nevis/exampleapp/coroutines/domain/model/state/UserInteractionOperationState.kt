/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.state

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.pin.PinEnrollmentHandler
import ch.nevis.mobile.sdk.api.operation.selection.AccountSelectionHandler
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionHandler
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.OsAuthenticationListenHandler
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerificationHandler
import kotlinx.coroutines.CancellableContinuation

/**
 * [OperationState] implementation for registration and authentication operations.
 */
data class UserInteractionOperationState(

    /**
     * Overridden operation property.
     */
    override val operation: Operation,

    /**
     * The username that is identifies the account used during the authentication if it can be
     * determined, otherwise null.
     */
    var username: String? = null,

    /**
     * [AccountSelectionHandler] object received during an out-of-band authentication operation.
     */
    var accountSelectionHandler: AccountSelectionHandler? = null,

    /**
     * [AccountSelectionHandler] object received during an authentication or a registration operation.
     */
    var authenticatorSelectionHandler: AuthenticatorSelectionHandler? = null,

    /**
     * [PinUserVerificationHandler] object received during an authentication operation.
     */
    var pinUserVerificationHandler: PinUserVerificationHandler? = null,

    /**
     * [PinEnrollmentHandler] object received during a registration operation.
     */
    var pinEnrollmentHandler: PinEnrollmentHandler? = null,

    /**
     * [FingerprintUserVerificationHandler] object received during an authentication or a registration operation.
     */
    var fingerprintUserVerificationHandler: FingerprintUserVerificationHandler? = null,

    /**
     * [BiometricUserVerificationHandler] object received during an authentication or a registration operation.
     */
    var biometricUserVerificationHandler: BiometricUserVerificationHandler? = null,

    /**
     * [OsAuthenticationListenHandler] object received during an authentication or a registration operation.
     */
    var osAuthenticationListenHandler: OsAuthenticationListenHandler? = null,

    /**
     * A [CancellableContinuation] instance of a `suspendCancellableCoroutine` Kotlin coroutine block.
     * The [CancellableContinuation] will be resumed when the SDK finished an operation step.
     */
    override var cancellableContinuation: CancellableContinuation<Response>? = null
) : OperationState() {
    override fun reset() {
        username = null
        authenticatorSelectionHandler = null
        pinUserVerificationHandler = null
        pinEnrollmentHandler = null
        fingerprintUserVerificationHandler = null
        biometricUserVerificationHandler = null
        cancellableContinuation = null
    }
}