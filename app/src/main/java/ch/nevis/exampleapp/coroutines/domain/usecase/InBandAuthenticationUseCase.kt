/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for starting an in-band authentication operation.
 */
interface InBandAuthenticationUseCase {

    /**
     * Executes the use-case.
     *
     * @param username The username that identifies the account that is used for the in-band authentication.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(username: String): Response
}
