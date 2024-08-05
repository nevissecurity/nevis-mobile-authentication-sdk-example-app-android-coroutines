/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for completing change PIN operation. It assumes that a change PIN operation is
 * already started with [StartChangePinUseCase].
 */
interface ChangePinUseCase {

    /**
     * Executes the use-case.
     *
     * @param oldPin The old PIN that should be changed.
     * @param newPin The new PIN.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(oldPin: CharArray, newPin: CharArray): Response
}
