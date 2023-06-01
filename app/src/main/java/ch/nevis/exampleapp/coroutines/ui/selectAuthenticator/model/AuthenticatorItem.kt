package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model

import androidx.annotation.StringRes
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.mobile.sdk.api.localdata.Authenticator

/**
 * Represents an authenticator that is listed and can be selected by the user on select authenticator view.
 *
 * @throws IllegalArgumentException when an unknown AAID is used.
 */
data class AuthenticatorItem(
    /**
     * The AAID of the authenticator.
     */
    val aaid: String,

    /**
     * The flag that tells whether the authenticator is server policy compliant or not.
     */
    val isPolicyCompliant: Boolean,

    /**
     * The flag that tells whether the user already enrolled the authenticator or not.
     */
    val isUserEnrolled: Boolean
) {
    /**
     * String resource identifier of the title of the authenticator.
     */
    @StringRes
    val titleResId = authenticatorTitles[aaid] ?: throw IllegalArgumentException("Unknown AAID.")

    /**
     * Tells that if this authenticator item is selectable on select authenticator view or not.
     * The value is calculated based on [AuthenticatorItem.isPolicyCompliant] and [AuthenticatorItem.isUserEnrolled] flags.
     */
    fun isEnabled(): Boolean {
        return isPolicyCompliant && (aaid == Authenticator.PIN_AUTHENTICATOR_AAID || isUserEnrolled)
    }

    //region Companion
    companion object {
        /**
         * A map that holds title String resource identifiers for each known authenticator identifier.
         */
        private val authenticatorTitles = mapOf(
            Authenticator.PIN_AUTHENTICATOR_AAID to R.string.select_authenticator_pin_authenticator_title,
            Authenticator.FINGERPRINT_AUTHENTICATOR_AAID to R.string.select_authenticator_fingerprint_authenticator_title,
            Authenticator.BIOMETRIC_AUTHENTICATOR_AAID to R.string.select_authenticator_biometric_authenticator_title
        )
    }
    //endregion
}
