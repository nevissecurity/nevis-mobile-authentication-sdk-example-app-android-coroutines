/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.password.PasswordAuthenticatorProtectionStatus
import ch.nevis.mobile.sdk.api.operation.password.PasswordChangeRecoverableError

/**
 * A [Response] class that indicates the running change password operation waiting for a new password.
 * To continue the operation after this response is received [ch.nevis.exampleapp.coroutines.domain.usecase.ChangePasswordUseCase]
 * has to be started with the old and new password entered by the user.
 *
 * @constructor Creates a new instance.
 * @param passwordAuthenticatorProtectionStatus The current status of the Password authenticator.
 * @param lastRecoverableError The last recoverable error. It exists only if there was already a failed
 *  password change attempt.
 */
data class ChangePasswordResponse(

    /**
     * The current status of the Password authenticator.
     */
    val passwordAuthenticatorProtectionStatus: PasswordAuthenticatorProtectionStatus,

    /**
     * The last recoverable error. It exists only if there was already a failed password change attempt.
     */
    val lastRecoverableError: PasswordChangeRecoverableError? = null
) : Response
