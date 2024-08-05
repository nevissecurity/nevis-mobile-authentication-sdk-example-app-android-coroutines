/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.Configuration

/**
 * Use-case interface for [ch.nevis.mobile.sdk.api.MobileAuthenticationClient] initialization.
 */
interface InitializeClientUseCase {

    /**
     * Executes the use-case.
     *
     * @param configuration The [Configuration] object that must be used for the client initialization.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(configuration: Configuration): Response
}
