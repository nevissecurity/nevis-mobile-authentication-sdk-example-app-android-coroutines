/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.settings

import android.content.Context
import ch.nevis.exampleapp.coroutines.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Default implementation of [Settings] interface.
 */
class SettingsImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : Settings {
    override val allowClass2Sensors = context.resources.getBoolean(R.bool.allow_class2_sensors)
}
