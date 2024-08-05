/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.sdk

import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus

/**
 * Implementation of [PinAuthenticatorProtectionStatus.LastAttemptFailed] interface.
 *
 * @constructor Creates a new instance.
 * @param remainingRetries The number of remaining retries available.
 * @param coolDownTimeInSeconds The time that must be passed before the user can try to provide credentials
 *      again.
 */
data class PinAuthenticatorProtectionStatusLastAttemptFailedImpl(
    /**
     * The number of remaining retries available.
     */
    val remainingRetries: Int,

    /**
     * The time that must be passed before the user can try to provide credentials again.
     * If the value is 0, it means that no cool-down is required, and that a new password can be provided
     * immediately.
     */
    val coolDownTimeInSeconds: Long
) : PinAuthenticatorProtectionStatus.LastAttemptFailed {

    //region PinAuthenticatorProtectionStatus.LastAttemptFailed
    /** @suppress */
    override fun remainingRetries(): Int = remainingRetries

    /** @suppress */
    override fun coolDownTimeInSeconds(): Long = coolDownTimeInSeconds
    //endregion
}
