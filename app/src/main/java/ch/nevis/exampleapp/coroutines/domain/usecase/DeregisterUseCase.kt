/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider

/**
 * Interface declaration of deregister use-case that starts deregistration of registered accounts, authenticators.
 */
interface DeregisterUseCase {

    /**
     * Executes use-case.
     *
     * @param username The username the deregistration process started for. If it is null, the deregistration will be started for all users/accounts.
     * @param authorizationProvider The [AuthorizationProvider] that should be used for the deregistration process.
     * @return A [Response] object, the result of the use-case execution.
     */
    suspend fun execute(
        username: String? = null,
        authorizationProvider: AuthorizationProvider? = null
    ): Response
}
