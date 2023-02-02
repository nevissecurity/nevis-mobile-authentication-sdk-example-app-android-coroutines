/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.GetAuthenticatorsResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Default implementation of [GetAuthenticatorsUseCase] interface that queries the authenticators from
 * the client and returns them in a [GetAuthenticatorsResponse].
 */
class GetAuthenticatorsUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider
) : GetAuthenticatorsUseCase {

    //region GetAuthenticatorsUseCase
    override suspend fun execute(): Response {
        val client = clientProvider.get()
        return if (client != null) {
            GetAuthenticatorsResponse(client.localData().authenticators())
        } else {
            ErrorResponse(BusinessException.clientNotInitialized())
        }
    }
    //endregion
}