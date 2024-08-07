/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.state

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.password.PasswordEnrollmentHandler
import ch.nevis.mobile.sdk.api.operation.pin.PinEnrollmentHandler
import ch.nevis.mobile.sdk.api.operation.selection.AccountSelectionHandler
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionHandler
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.OsAuthenticationListenHandler
import ch.nevis.mobile.sdk.api.operation.userverification.PasswordUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerificationHandler
import kotlinx.coroutines.CancellableContinuation

/**
 * [OperationState] implementation for registration and authentication operations.
 *
 * @constructor Creates a new instance.
 * @param operation The [Operation] the state relates to.
 * @param username The username that is identifies the account used during the authentication if it
 *  can be determined, otherwise null.
 * @param accountSelectionHandler [AccountSelectionHandler] object received during an out-of-band
 *  authentication operation.
 * @param authenticatorSelectionHandler [AuthenticatorSelectionHandler] object received during an
 *  authentication or a registration operation.
 * @param pinEnrollmentHandler [PinEnrollmentHandler] object received during a registration operation.
 * @param passwordEnrollmentHandler [PasswordEnrollmentHandler] object received during a registration
 *  operation.
 * @param pinUserVerificationHandler [PinUserVerificationHandler] object received during an authentication
 *  operation.
 * @param passwordUserVerificationHandler [PasswordUserVerificationHandler] object received during an
 *  authentication operation.
 * @param fingerprintUserVerificationHandler [FingerprintUserVerificationHandler] object received during
 *  an authentication or a registration operation.
 * @param biometricUserVerificationHandler [BiometricUserVerificationHandler] object received during
 *  an authentication or a registration operation.
 * @param devicePasscodeUserVerificationHandler [DevicePasscodeUserVerificationHandler] object received
 *  during an authentication or a registration operation.
 * @param osAuthenticationListenHandler [OsAuthenticationListenHandler] object received during an authentication
 *  or a registration operation.
 * @param cancellableContinuation A [CancellableContinuation] instance of a `suspendCancellableCoroutine`
 *  Kotlin coroutine block.
 */
data class UserInteractionOperationState(
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
     * [AuthenticatorSelectionHandler] object received during an authentication or a registration operation.
     */
    var authenticatorSelectionHandler: AuthenticatorSelectionHandler? = null,

    /**
     * [PinEnrollmentHandler] object received during a registration operation.
     */
    var pinEnrollmentHandler: PinEnrollmentHandler? = null,

    /**
     * [PasswordEnrollmentHandler] object received during a registration operation.
     */
    var passwordEnrollmentHandler: PasswordEnrollmentHandler? = null,

    /**
     * [PinUserVerificationHandler] object received during an authentication operation.
     */
    var pinUserVerificationHandler: PinUserVerificationHandler? = null,

    /**
     * [PasswordUserVerificationHandler] object received during an authentication operation.
     */
    var passwordUserVerificationHandler: PasswordUserVerificationHandler? = null,

    /**
     * [FingerprintUserVerificationHandler] object received during an authentication or a registration operation.
     */
    var fingerprintUserVerificationHandler: FingerprintUserVerificationHandler? = null,

    /**
     * [BiometricUserVerificationHandler] object received during an authentication or a registration operation.
     */
    var biometricUserVerificationHandler: BiometricUserVerificationHandler? = null,

    /**
     * [DevicePasscodeUserVerificationHandler] object received during an authentication or a registration operation.
     */
    var devicePasscodeUserVerificationHandler: DevicePasscodeUserVerificationHandler? = null,

    /**
     * [OsAuthenticationListenHandler] object received during an authentication or a registration operation.
     */
    var osAuthenticationListenHandler: OsAuthenticationListenHandler? = null,

    override var cancellableContinuation: CancellableContinuation<Response>? = null
) : OperationState() {
    override fun reset() {
        username = null
        authenticatorSelectionHandler = null
        pinEnrollmentHandler = null
        passwordEnrollmentHandler = null
        pinUserVerificationHandler = null
        passwordUserVerificationHandler = null
        fingerprintUserVerificationHandler = null
        biometricUserVerificationHandler = null
        devicePasscodeUserVerificationHandler = null
        cancellableContinuation = null
    }
}
