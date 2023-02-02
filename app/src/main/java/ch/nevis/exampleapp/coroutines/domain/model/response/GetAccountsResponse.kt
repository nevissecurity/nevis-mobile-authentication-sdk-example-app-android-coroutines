/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.localdata.Account

/**
 * A [Response] class that holds the requested [Account] set.
 */
data class GetAccountsResponse(

    /**
     * The requested [Account] set.
     */
    val accounts: Set<Account>
) : Response
