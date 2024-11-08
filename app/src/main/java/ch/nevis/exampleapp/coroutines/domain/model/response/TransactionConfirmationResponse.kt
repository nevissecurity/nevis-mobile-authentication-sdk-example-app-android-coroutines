/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.localdata.Account

/**
 * [Response] class that indicates a transaction confirmation has to be started.
 * After the transaction confirmation, [ch.nevis.exampleapp.coroutines.domain.usecase.SelectAccountUseCase]
 * should be called with the contained account to continue the operation.
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
    val account: Account,

    /**
     * The optional transaction data/message that is sent during an authentication process.
     */
    val transactionConfirmationMessage: String
) : Response
