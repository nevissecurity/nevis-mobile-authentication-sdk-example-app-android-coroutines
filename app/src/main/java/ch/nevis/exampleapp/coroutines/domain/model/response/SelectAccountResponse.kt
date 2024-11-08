/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022-2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.mobile.sdk.api.localdata.Account

/**
 * [Response] class that indicates an account selection has to be started.
 * Typically the received [Account] set is shown to the user and he/she selects one of them.
 * After the account selection [ch.nevis.exampleapp.coroutines.domain.usecase.SelectAccountUseCase]
 * is called to continue the operation.
 *
 * @constructor Creates a new instance.
 * @param operation The [Operation] the account selection requested for.
 * @param accounts The set of enrolled accounts the user has to choose from.
 * @param transactionConfirmationMessage The optional transaction data/message that might be sent
 * during an authentication process.
 */
class SelectAccountResponse(

    /**
     * The [Operation] the account selection requested for.
     */
    val operation: Operation,

    /**
     * The set of enrolled accounts the user has to choose from.
     */
    val accounts: Set<Account>,

    /**
     * The optional transaction data/message that is sent during an authentication process.
     */
    val transactionConfirmationMessage: String? = null
) : Response
