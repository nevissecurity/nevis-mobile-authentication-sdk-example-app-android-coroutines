/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus

/**
 * Response class that indicates the SDK operation asks the user to verify herself/himself using PIN
 * authentication. Typically when this response is received a [ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPinUseCase]
 * is started.
 *
 * @constructor Creates a new instance.
 * @param pinAuthenticatorProtectionStatus Status object of the PIN authenticator.
 */
class VerifyPinResponse(
    /**
     * Status object of the PIN authenticator.
     */
    val pinAuthenticatorProtectionStatus: PinAuthenticatorProtectionStatus
) : Response
