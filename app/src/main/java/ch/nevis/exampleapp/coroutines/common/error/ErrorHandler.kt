/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.error

/**
 * Interface declaration of an error handler that can handle or bypass a given error.
 */
interface ErrorHandler {

    /**
     * Handles or bypasses the given error.
     *
     * @param error The error to be handled.
     * @return True if the error was handled by this [ErrorHandler] or false if it was bypassed.
     */
    fun handle(error: Throwable): Boolean
}
