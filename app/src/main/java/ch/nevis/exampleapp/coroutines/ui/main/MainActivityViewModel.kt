/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.nevis.exampleapp.coroutines.domain.log.LogItem
import ch.nevis.exampleapp.coroutines.domain.log.SdkLogReceiver
import ch.nevis.exampleapp.coroutines.domain.log.SdkLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * View model implementation of Main activity.
 *
 * @constructor Creates a new instance.
 * @param sdkLogger An injected instance of an implementation of [SdkLogger] interface.
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sdkLogger: SdkLogger
): ViewModel(), SdkLogReceiver {

    //region Initialization
    /**
     * Initialization.
     */
    init {
        // At initialization time we add this view model to the [SDKLogger] as a log receiver.
        sdkLogger.addLogReceiver(this)
    }
    //endregion

    //region Properties
    /**
     * Log [LiveData] that is used to post new [LogItem] objects to observers.
     */
    private val _log = MutableLiveData<LogItem>()

    /**
     * Log [LiveData] that is used to post new [LogItem] objects to observers.
     */
    val log: LiveData<LogItem> = _log
    //endregion

    //region ViewModel
    override fun onCleared() {
        super.onCleared()

        // Removing this view model from log receivers of the [SDKLogger].
        sdkLogger.removeLogReceiver(this)
    }
    //endregion

    //region LogReceiver
    override fun newLogItem(logItem: LogItem) {
        _log.postValue(logItem)
    }
    //endregion
}
