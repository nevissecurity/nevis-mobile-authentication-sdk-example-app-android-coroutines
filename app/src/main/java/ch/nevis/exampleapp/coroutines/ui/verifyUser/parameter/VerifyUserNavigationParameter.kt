/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.verifyUser.parameter

import android.os.Parcelable
import ch.nevis.exampleapp.coroutines.ui.verifyUser.model.VerifyUserViewMode
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter data class for Verify User view.
 */
@Parcelize
data class VerifyUserNavigationParameter(
    /**
     * The mode, the Verify User view intend to be used/initialized.
     */
    val verifyUserViewMode: VerifyUserViewMode
) : Parcelable
