/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model

import androidx.annotation.StringRes
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
    val isUserEnrolled: Boolean,

    /**
     * String resource identifier of the title of the authenticator.
     */
    @StringRes
    val titleResId: Int
) {
    /**
     * Tells that if this authenticator item is selectable on select authenticator view or not.
     * The value is calculated based on [AuthenticatorItem.isPolicyCompliant] and [AuthenticatorItem.isUserEnrolled] flags.
     */
    fun isEnabled(): Boolean {
        return isPolicyCompliant && (
            aaid == Authenticator.PIN_AUTHENTICATOR_AAID ||
            aaid == Authenticator.PASSWORD_AUTHENTICATOR_AAID ||
            isUserEnrolled
        )
    }
}
