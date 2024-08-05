/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.util

import android.content.Context
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus

/**
 * Extension function of [PinAuthenticatorProtectionStatus] to get the localized description.
 *
 * @param context An Android [Context] object for [String] resource resolving.
 * @return The localized description of the instance.
 */
fun PinAuthenticatorProtectionStatus.message(context: Context): String {
    return when (this) {
        is PinAuthenticatorProtectionStatus.Unlocked -> String()
        is PinAuthenticatorProtectionStatus.LockedOut -> context.getString(R.string.pin_protection_status_locked_out)
        is PinAuthenticatorProtectionStatus.LastAttemptFailed -> {
            when (remainingRetries()) {
                1 -> {
                    if (coolDownTimeInSeconds() == 0L) {
                        return context.getString(R.string.pin_protection_status_last_retry_without_cool_down)
                    } else {
                        return context.getString(R.string.pin_protection_status_last_retry_with_cool_down, coolDownTimeInSeconds())
                    }
                }
                else -> {
                    if (coolDownTimeInSeconds() == 0L) {
                        return context.getString(R.string.pin_protection_status_retries_without_cool_down, remainingRetries())
                    } else {
                        return context.getString(R.string.pin_protection_status_retries_with_cool_down, remainingRetries(), coolDownTimeInSeconds())
                    }
                }
            }
        }
        else -> throw IllegalStateException("Unsupported PIN authenticator protection status.")
    }
}
