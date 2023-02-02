/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.error.MobileAuthenticationClientException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.CompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Default implementation of [DeregisterUseCase] interface.
 */
class DeregisterUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider
) : DeregisterUseCase {

    //region DeregisterUseCase
    override suspend fun execute(
        username: String?,
        authorizationProvider: AuthorizationProvider?
    ): Response {
        var response: Response? = null
        val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
        val localData = client.localData()

        username?.also {
            response = deregister(it, authorizationProvider)
        } ?: run {
            for (account in localData.accounts()) {
                response =
                    deregister(account.username(), authorizationProvider)
                if (response is ErrorResponse) {
                    break
                }
            }
        }
        return response ?: CompletedResponse(Operation.DEREGISTRATION)
    }
    //endregion

    //region Private Interface
    /**
     * Starts a single deregistration operation call and deregister a user/account.
     *
     * @param username The username of the user that is the owner of the authenticator.
     * @return A [Response] object that indicates the result of this single deregistration operation.
     */
    private suspend fun deregister(
        username: String,
        authorizationProvider: AuthorizationProvider? = null
    ): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            val operation = client.operations().deregistration()
                .username(username)
                .onSuccess {
                    cancellableContinuation.resume(
                        CompletedResponse(Operation.DEREGISTRATION)
                    )
                }
                .onError {
                    cancellableContinuation.resume(
                        ErrorResponse(
                            MobileAuthenticationClientException(
                                Operation.DEREGISTRATION,
                                it
                            )
                        )
                    )
                }

            authorizationProvider?.let {
                operation.authorizationProvider(authorizationProvider)
            }

            operation.execute()
        }
    }
    //endregion
}