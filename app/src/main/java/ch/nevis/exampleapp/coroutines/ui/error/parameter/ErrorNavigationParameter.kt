/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.error.parameter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter data class for Error view.
 */
@Parcelize
data class ErrorNavigationParameter(

    /**
     * The error message to be displayed on Error view.
     */
    val message: String?
) : Parcelable
