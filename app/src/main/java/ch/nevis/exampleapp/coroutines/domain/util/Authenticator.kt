/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.util

import ch.nevis.exampleapp.coroutines.R
import ch.nevis.mobile.sdk.api.localdata.Authenticator
import ch.nevis.mobile.sdk.api.localdata.Authenticator.*
import ch.nevis.mobile.sdk.api.localdata.UserEnrollment

/**
 * Extension function of [Authenticator] to check if it's enrolled for the given user/account or not.
 *
 * @param username The username that identifies the user/account.
 * @param allowClass2Sensors A flag that tells whether using Class 2 biometric sensors is allowed
 * if the biometric authenticator is selected.
 * @return A flag that tells whether the authenticator is enrolled for the user or not.
 * @throws IllegalStateException if the [Authenticator] has an unknown [UserEnrollment] implementation.
 */
fun Authenticator.isUserEnrolled(username: String, allowClass2Sensors: Boolean): Boolean {
    return when (val userEnrollment = userEnrollment()) {
        is UserEnrollment.OsUserEnrollment -> {
            if (allowClass2Sensors) {
                userEnrollment.isEnrolledWithClass2OrClass3Sensor
            } else {
                userEnrollment.isEnrolled
            }
        }
        is UserEnrollment.SdkUserEnrollment -> userEnrollment.isEnrolled(username)
        else -> throw IllegalStateException("Unknown UserEnrollment object.")
    }
}

/**
 * Extension function of [Authenticator] to get the title String resource identifier.
 *
 * @return The title String resource identifier.
 * @throws IllegalStateException if the [Authenticator] has an unknown AAID.
 */
fun Authenticator.titleResId(): Int {
    return when (val aaid = aaid()) {
        PIN_AUTHENTICATOR_AAID -> R.string.authenticator_pin_title
        PASSWORD_AUTHENTICATOR_AAID -> R.string.authenticator_password_title
        BIOMETRIC_AUTHENTICATOR_AAID -> R.string.authenticator_biometric_title
        FINGERPRINT_AUTHENTICATOR_AAID -> R.string.authenticator_fingerprint_title
        DEVICE_PASSCODE_AUTHENTICATOR_AAID -> R.string.authenticator_device_passcode_title
        else -> throw IllegalStateException("Unsupported authenticator with AAID '$aaid'.")
    }
}
