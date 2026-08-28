/*
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2026. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.NoPendingOperationsFoundResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.PendingOperationsFoundResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload

/**
 * Use-case interface for fetching the pending out-of-band operations of the registered accounts.
 */
interface FetchPendingOperationsUseCase {

    /**
     * Executes the use-case.
     *
     * @return A [PendingOperationsFoundResponse] object
     *  holding the [OutOfBandPayload] of the latest pending
     *  out-of-band operation, or a [NoPendingOperationsFoundResponse]
     *  object if there is no pending out-of-band operation.
     */
    suspend fun execute(): Response
}
