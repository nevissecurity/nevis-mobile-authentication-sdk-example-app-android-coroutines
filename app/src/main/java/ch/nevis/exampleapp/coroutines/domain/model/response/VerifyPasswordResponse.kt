/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.password.PasswordAuthenticatorProtectionStatus

/**
 * Response class that indicates the SDK operation asks the user to verify herself/himself using Password
 * authentication. Typically when this response is received a [ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPasswordUseCase]
 * is started.
 *
 * @constructor Creates a new instance.
 * @param passwordAuthenticatorProtectionStatus Status object of the Password authenticator.
 */
class VerifyPasswordResponse(
    /**
     * Status object of the Password authenticator.
     */
    val passwordAuthenticatorProtectionStatus: PasswordAuthenticatorProtectionStatus
) : Response
