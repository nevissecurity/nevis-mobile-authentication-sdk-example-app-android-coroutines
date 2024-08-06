/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.localdata.Account

/**
 * Interface declaration of delete local authenticators use-case.
 */
interface DeleteAuthenticatorsUseCase {

    /**
     * Executes use-case.
     *
     * @param accounts The set of enrolled accounts.
     * @return A [Response] object, the result of the use-case execution.
     */
    suspend fun execute(
        accounts: Set<Account>
    ): Response
}
