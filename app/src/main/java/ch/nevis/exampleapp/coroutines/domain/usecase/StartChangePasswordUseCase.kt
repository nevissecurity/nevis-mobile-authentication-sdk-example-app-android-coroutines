/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case for starting a change Password operation.
 */
interface StartChangePasswordUseCase {

    /**
     * Executes the use-case.
     *
     * @param username The username/account to start the change password operation for.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(username: String): Response
}
