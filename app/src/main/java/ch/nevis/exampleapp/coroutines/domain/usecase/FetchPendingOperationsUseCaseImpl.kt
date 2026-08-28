/*
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2026. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.NoPendingOperationsFoundResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.PendingOperationsFoundResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.outofband.PendingOutOfBandOperation
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [FetchPendingOperationsUseCase] interface.
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of a [ClientProvider] implementation.
 */
class FetchPendingOperationsUseCaseImpl(private val clientProvider: ClientProvider) : FetchPendingOperationsUseCase {

    //region FetchPendingOperationsUseCase
    override suspend fun execute(): Response {
        val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
        val accounts = client.localData().accounts()
        if (accounts.isEmpty()) {
            throw BusinessException.accountsNotFound()
        }

        val pendingOperations = fetchPendingOutOfBandOperations()
        // The operations are sorted by creation time, the last one is the latest.
        return pendingOperations.lastOrNull()?.let {
            PendingOperationsFoundResponse(it.payload())
        } ?: NoPendingOperationsFoundResponse()
    }
    //endregion

    //region Private Interface

    /**
     * Fetches the pending out-of-band operations of the registered accounts.
     *
     * @return The list of [PendingOutOfBandOperation] objects.
     */
    private suspend fun fetchPendingOutOfBandOperations(): List<PendingOutOfBandOperation> =
        suspendCancellableCoroutine { cancellableContinuation ->
            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            client.operations().pendingOutOfBandOperations().onResult {
                cancellableContinuation.resume(it.operations().toList())
            }.execute()
        }
    //endregion
}
