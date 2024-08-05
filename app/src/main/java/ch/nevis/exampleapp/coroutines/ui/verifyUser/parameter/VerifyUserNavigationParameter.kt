/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.verifyUser.parameter

import android.os.Parcelable
import androidx.annotation.StringRes
import ch.nevis.exampleapp.coroutines.ui.verifyUser.model.VerifyUserViewMode
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter data class for Verify User view.
 *
 * @constructor Creates a new instance.
 * @param verifyUserViewMode The mode, the Verify User view intend to be used/initialized.
 * @param authenticatorTitleResId String resource identifier of the title of the authenticator.
 */
@Parcelize
data class VerifyUserNavigationParameter(
    /**
     * The mode, the Verify User view intend to be used/initialized.
     */
    val verifyUserViewMode: VerifyUserViewMode,

    /**
     * String resource identifier of the title of the authenticator.
     */
    @StringRes
    val authenticatorTitleResId: Int,
) : Parcelable
