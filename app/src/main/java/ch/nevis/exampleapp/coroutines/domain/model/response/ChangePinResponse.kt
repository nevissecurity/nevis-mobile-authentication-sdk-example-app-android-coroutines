/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus
import ch.nevis.mobile.sdk.api.operation.pin.PinChangeRecoverableError

/**
 * A [Response] class that indicates the running change PIN operation waiting for a new PIN.
 * To continue the operation after this response is received [ch.nevis.exampleapp.coroutines.domain.usecase.ChangePinUseCase]
 * has to be started with the old and new PIN entered by the user.
 *
 * @constructor Creates a new instance.
 * @param pinAuthenticatorProtectionStatus The current status of the PIN authenticator.
 * @param lastRecoverableError The last recoverable error. It exists only if there was already a failed
 *  PIN change attempt.
 */
data class ChangePinResponse(

    /**
     * The current status of the PIN authenticator.
     */
    val pinAuthenticatorProtectionStatus: PinAuthenticatorProtectionStatus,

    /**
     * The last recoverable error. It exists only if there was already a failed PIN change attempt.
     */
    val lastRecoverableError: PinChangeRecoverableError? = null
) : Response
