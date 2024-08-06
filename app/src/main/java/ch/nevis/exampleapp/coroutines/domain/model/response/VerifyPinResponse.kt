/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerificationError

/**
 * Response class that indicates the SDK operation asks the user to verify herself/himself using PIN
 * authentication. Typically when this response is received a [ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPinUseCase]
 * is started.
 *
 * @constructor Creates a new instance.
 * @param lastRecoverableError The last recoverable error. It exists only if there was already a failed
 *  PIN verification attempt.
 * @param pinAuthenticatorProtectionStatus Status object of the PIN authenticator.
 */
class VerifyPinResponse(
    /**
     * The last recoverable error. It exists only if there was already a failed PIN verification attempt.
     */
    val lastRecoverableError: PinUserVerificationError?,

    /**
     * Status object of the PIN authenticator.
     */
    val pinAuthenticatorProtectionStatus: PinAuthenticatorProtectionStatus
) : Response
