/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.localdata.Account

/**
 * Use-case interface for transaction confirmation.
 */
interface TransactionConfirmationUseCase {

    /**
     * Executes the use-case.
     *
     * @param account The account that is used for the out-of-band authentication.
     * @param transactionConfirmationMessage The transaction data/message that is be sent during an
     * authentication process.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(account: Account, transactionConfirmationMessage: String): Response
}
