/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.result.parameter

import android.os.Parcelable
import androidx.annotation.StringRes
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter data class for Result view.
 *
 * @constructor Creates a new instance.
 * @param titleResId String resource identifier of the title of Result screen.
 * @param operation The related operation if there is any.
 */
@Parcelize
data class ResultNavigationParameter(
    /**
     * String resource identifier of the title of Result screen.
     */
    @StringRes
    val titleResId: Int,

    /**
     * The related operation if there is any.
     */
    val operation: Operation? = null
) : Parcelable {

    //region Public Static Interface
    /**
     * Collection of public static methods.
     */
    companion object {

        /**
         * Creates a [ResultNavigationParameter] instance for general cancelled operation case.
         *
         * @param operation The related operation if there is any.
         * @return The created [ResultNavigationParameter] object.
         */
        fun forCancelledOperation(operation: Operation? = null): ResultNavigationParameter {
            return ResultNavigationParameter(
                R.string.result_title_cancelled,
                operation
            )
        }

        /**
         * Creates a [ResultNavigationParameter] instance for general successful operation case.
         *
         * @param operation The related operation if there is any.
         * @return The created [ResultNavigationParameter] object.
         */
        fun forSuccessfulOperation(operation: Operation? = null): ResultNavigationParameter {
            return ResultNavigationParameter(
                R.string.result_title_success,
                operation
            )
        }
    }
    //endregion
}
