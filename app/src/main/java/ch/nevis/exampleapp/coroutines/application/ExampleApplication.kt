/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022-2026. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.application

import android.app.Application
import ch.nevis.exampleapp.coroutines.domain.log.SdkLogger
import ch.nevis.exampleapp.coroutines.timber.ExampleAppTree
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

/**
 * Simple sub-class of [Application] to enable Dagger Hilt capabilities and to initialize logging.
 */
@HiltAndroidApp
class ExampleApplication : Application() {

    /**
     * An injected instance of an implementation of [SdkLogger] interface.
     */
    @Inject
    lateinit var sdkLogger: SdkLogger

    //region Application
    /** @suppress */
    override fun onCreate() {
        super.onCreate()

        Timber.plant(ExampleAppTree(sdkLogger))
    }
    //endregion
}
