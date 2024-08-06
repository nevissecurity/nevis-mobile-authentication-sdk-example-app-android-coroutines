/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.password.PasswordEnrollmentError

/**
 * A [Response] class that indicates the registration operation is waiting for password enrollment.
 * To continue the operation after this response is received [ch.nevis.exampleapp.coroutines.domain.usecase.SetPasswordUseCase]
 * has to be started with the new password entered by the user.
 *
 * @constructor Creates a new instance.
 * @param lastRecoverableError The last recoverable error. It exists only if there was already a failed
 *  password enrollment attempt.
 */
class EnrollPasswordResponse(
    /**
     * The last recoverable error. It exists only if there was already a failed password enrollment attempt.
     */
    val lastRecoverableError: PasswordEnrollmentError?
) : Response
