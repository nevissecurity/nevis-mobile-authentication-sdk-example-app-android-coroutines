/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for completing change Password operation. It assumes that a change Password operation
 * is already started with [StartChangePasswordUseCase].
 */
interface ChangePasswordUseCase {

    /**
     * Executes the use-case.
     *
     * @param oldPassword The old password that should be changed.
     * @param newPassword The new password.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(oldPassword: CharArray, newPassword: CharArray): Response
}
