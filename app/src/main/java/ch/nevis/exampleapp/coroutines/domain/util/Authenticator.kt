package ch.nevis.exampleapp.coroutines.domain.util

import ch.nevis.mobile.sdk.api.localdata.Authenticator
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
