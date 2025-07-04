/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.response.TransactionConfirmationResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.mobile.sdk.api.localdata.Account
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Default implementation of [TransactionConfirmationUseCase] interface.
 *
 * @constructor Creates a new instance.
 */
class TransactionConfirmationUseCaseImpl : TransactionConfirmationUseCase {

    //region TransactionConfirmationUseCase
    override suspend fun execute(account: Account, transactionConfirmationMessage: String): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            cancellableContinuation.resume(
                TransactionConfirmationResponse(
                    account,
                    transactionConfirmationMessage
                )
            )
        }
    }
    //endregion
}
