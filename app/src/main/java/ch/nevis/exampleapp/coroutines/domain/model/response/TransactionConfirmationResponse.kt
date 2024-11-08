/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.localdata.Account

/**
 * [Response] class that indicates a transaction confirmation has to be started.
 * Typically the received [Account] set is shown to the user and he/she selects one of them.
 * After the account selection [ch.nevis.exampleapp.coroutines.domain.usecase.SelectAccountUseCase] is called
 * to continue the operation.
 *
 * @constructor Creates a new instance.
 * @param account The previously selected account.
 * @param transactionConfirmationMessage The transaction data/message that is be sent during an
 * authentication process.
 */
class TransactionConfirmationResponse (

    /**
     * The previously selected account.
     */
    val account: Account? = null,

    /**
     * The optional transaction data/message that is sent during an authentication process.
     */
    val transactionConfirmationMessage: String
) : Response
