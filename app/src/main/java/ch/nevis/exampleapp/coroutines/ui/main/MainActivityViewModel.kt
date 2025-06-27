/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.log.LogItem
import ch.nevis.exampleapp.coroutines.domain.log.SdkLogReceiver
import ch.nevis.exampleapp.coroutines.domain.log.SdkLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
        // At initialization time we add this view model to the [SdkLogger] as a log receiver.
        sdkLogger.addLogReceiver(this)
    }

    //endregion

    //region Properties
    /**
     * Internal [Channel] used to buffer and deliver [LogItem] events to observers.
     */
    private val _log = Channel<LogItem>(Channel.BUFFERED)

    /**
     * Hot [Flow] of [LogItem]s representing log events emitted by the application.
     * Each collected value is a new log event.
     */
    val log: Flow<LogItem> = _log.receiveAsFlow()
    //endregion

    //region ViewModel
    override fun onCleared() {
        super.onCleared()

        // Removing this view model from log receivers of the [SdkLogger].
        sdkLogger.removeLogReceiver(this)

        // Close the channel of [LogItem] events.
        _log.close()
    }
    //endregion

    //region LogReceiver
    override fun newLogItem(logItem: LogItem) {
        viewModelScope.launch {
            _log.send(logItem)
        }
    }
    //endregion
}
