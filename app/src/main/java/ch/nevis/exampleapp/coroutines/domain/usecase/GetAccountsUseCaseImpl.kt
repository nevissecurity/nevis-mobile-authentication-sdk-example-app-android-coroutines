/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.GetAccountsResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Default implementation of [GetAccountsUseCase] interface that queries the registered accounts from
 * the client and returns them in a [GetAccountsResponse].
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of [ClientProvider] interface implementation.
 */
class GetAccountsUseCaseImpl(
    private val clientProvider: ClientProvider
) : GetAccountsUseCase {

    //region GetAccountsUseCase
    override suspend fun execute(): Response {
        val client = clientProvider.get()
        return if (client != null) {
            GetAccountsResponse(client.localData().accounts())
        } else {
            ErrorResponse(BusinessException.clientNotInitialized())
        }
    }
    //endregion
}
