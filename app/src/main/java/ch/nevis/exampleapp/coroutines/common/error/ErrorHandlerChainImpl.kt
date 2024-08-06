/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.error

/**
 * Default implementation of [ErrorHandlerChain] interface.
 *
 * @constructor Creates a new instance.
 */
class ErrorHandlerChainImpl : ErrorHandlerChain {

    //region Properties
    private val errorHandlerChain = mutableListOf<ErrorHandler>()
    //endregion

    //region ErrorHandlerChain
    override fun add(errorHandler: ErrorHandler) {
        errorHandlerChain.add(errorHandler)
    }

    override fun remove(errorHandler: ErrorHandler): Boolean {
        return errorHandlerChain.remove(errorHandler)
    }

    override fun removeAll() {
        errorHandlerChain.clear()
    }

    override fun handle(error: Throwable) {
        for (errorHandler in errorHandlerChain) {
            if (errorHandler.handle(error)) {
                // The error was handled by the current errorHandler. Breaking the chain.
                break
            }
        }
    }
    //endregion
}
