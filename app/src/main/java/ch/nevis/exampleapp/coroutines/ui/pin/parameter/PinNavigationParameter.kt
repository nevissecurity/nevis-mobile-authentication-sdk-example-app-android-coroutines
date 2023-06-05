/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.pin.parameter

import android.os.Parcelable
import ch.nevis.exampleapp.coroutines.ui.pin.model.PinViewMode
import ch.nevis.mobile.sdk.api.operation.RecoverableError
import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter data class for PIN view.
 */
@Parcelize
data class PinNavigationParameter(
    /**
     * The mode, the PIN view intend to be used/initialized.
     */
    val pinViewMode: PinViewMode,

    /**
     * Status object of the PIN authenticator.
     */
    @IgnoredOnParcel
    val pinAuthenticatorProtectionStatus: PinAuthenticatorProtectionStatus? = null,

    /**
     * The last recoverable error. It exists only if there was already a failed PIN operation attempt.
     */
    @IgnoredOnParcel
    val lastRecoverableError: RecoverableError? = null
) : Parcelable
