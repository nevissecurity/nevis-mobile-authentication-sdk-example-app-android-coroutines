/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.log

import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * Default implementation of [SdkLogger] interface. It simply stores the logged messages in an [ArrayList] object.
 *
 * @constructor Creates a new instance.
 */
class SdkLoggerImpl: SdkLogger {

    //region Properties
    /**
     * The SDK event log. A simple [ArrayList] that contains the log items.
     */
    private var log = ArrayList<LogItem>()

    /**
     * A list of receiver those should be notified about new messages.
     */
    private var logReceivers = ArrayList<WeakReference<SdkLogReceiver>>()
    //endregion

    //region SdkEventLog
    override fun addLogReceiver(logReceiver: SdkLogReceiver) {
        logReceivers.add(WeakReference(logReceiver))
    }

    override fun removeLogReceiver(logReceiver: SdkLogReceiver) {
        for (logReceiverWeakReference in logReceivers) {
            if (logReceiver == logReceiverWeakReference.get()) {
                logReceivers.remove(logReceiverWeakReference)
                break
            }
        }
    }

    override fun log(message: String) {
        val logItem = LogItem(Date(), message)
        log.add(logItem)
        logReceivers.forEach {
            it.get()?.newLogItem(logItem)
        }
    }
    //endregion
}
