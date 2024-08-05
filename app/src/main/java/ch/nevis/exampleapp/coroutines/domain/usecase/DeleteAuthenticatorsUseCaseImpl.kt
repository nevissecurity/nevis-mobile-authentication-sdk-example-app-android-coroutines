/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.CompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.localdata.Account
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Default implementation of [DeleteAuthenticatorsUseCase] interface.
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of a [ClientProvider] implementation.
 */
class DeleteAuthenticatorsUseCaseImpl(
    private val clientProvider: ClientProvider
) : DeleteAuthenticatorsUseCase {

    //region DeleteAuthenticatorsUseCase
    override suspend fun execute(accounts: Set<Account>): Response {
        for (account in accounts) {
            deleteAuthenticators(account.username())
        }

        return CompletedResponse(Operation.LOCAL_DATA)
    }
    //endregion

    //region Private Interface
    /**
     * Deletes all local authenticators of an enrolled account.
     *
     * @param username The username that identifies the account whose authenticators must be deleted locally.
     */
    private suspend fun deleteAuthenticators(username: String) {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            client.localData().deleteAuthenticator(username)
            cancellableContinuation.resume(Unit)
        }
    }
    //endregion
}
