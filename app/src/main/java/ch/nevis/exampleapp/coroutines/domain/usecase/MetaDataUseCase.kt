/*
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2025. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case for retrieving the metadata of Nevis Mobile Authentication SDK.
 */
interface MetaDataUseCase {

    /**
     * Executes the use-case.
     *
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(): Response
}
