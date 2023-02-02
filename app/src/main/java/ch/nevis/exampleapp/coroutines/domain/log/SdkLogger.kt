/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.log

/**
 * An interface declaration of a SDK event logger. Implement this interface to be able to log SDK events
 * and to be able to notify receivers about new messages
 */
interface SdkLogger {

    /**
     * Adds a new receiver object to this logger. In case of a new message, all added receivers will be notified.
     *
     * @param logReceiver The log receiver instance.
     */
    fun addLogReceiver(logReceiver: SdkLogReceiver)

    /**
     * Removes a receiver object to this logger. The removed receiver will not be notified anymore about new messages.
     *
     * @param logReceiver The log receiver instance.
     */
    fun removeLogReceiver(logReceiver: SdkLogReceiver)

    /**
     * Logs a new message.
     */
    fun log(message: String)
}