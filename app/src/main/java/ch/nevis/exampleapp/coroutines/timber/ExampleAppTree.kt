/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.timber

import android.util.Log
import ch.nevis.exampleapp.coroutines.domain.log.SdkLogger
import timber.log.Timber

/**
 * An example application specific sub-class of [Timber.DebugTree] that logs SDK events at a specific priority level.
 */
class ExampleAppTree(

    /**
     * An instance of an implementation of [SdkLogger] interface.
     */
    private val sdkLogger: SdkLogger
): Timber.DebugTree() {

    //region Constants
    companion object {
        /**
         * Priority level constant for SDK events.
         */
        internal const val PRIORITY_SDK_EVENT = 100
    }
    //endregion

    //region Timber.DebugTree
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == PRIORITY_SDK_EVENT) {
            sdkLogger.log(message)
        }
        super.log(Log.DEBUG, tag, message, t)
    }
    //endregion
}
