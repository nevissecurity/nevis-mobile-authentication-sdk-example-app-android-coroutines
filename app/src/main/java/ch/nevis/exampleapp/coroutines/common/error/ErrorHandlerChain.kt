/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.error

/**
 * Interface declaration of error handler chain that can delegate the handling of the errors to
 * the configured [ErrorHandler] instances.
 *
 * An error handler chain is used to ease the separation of handling each error types if necessary.
 * Each [ErrorHandler] implementation can decide whether it wants to handle the actual error that occurred
 * or it wants to bypass it to the next [ErrorHandler] in the chain.
 */
interface ErrorHandlerChain {

    /**
     * Adds an [ErrorHandler] instance to the end of the chain.
     *
     * @param errorHandler The [ErrorHandler] to be added to the chain.
     */
    fun add(errorHandler: ErrorHandler)

    /**
     * Removes the given [ErrorHandler] instance from the chain.
     *
     * @param errorHandler The [ErrorHandler] to be removed from the chain.
     */
    fun remove(errorHandler: ErrorHandler): Boolean

    /**
     * Removes all [ErrorHandler] instances from the chain.
     */
    fun removeAll()

    /**
     * Handles the given error. Delegates it to the configured [ErrorHandler] instances.
     *
     * @param error The error to be handled.
     */
    fun handle(error: Throwable)
}
