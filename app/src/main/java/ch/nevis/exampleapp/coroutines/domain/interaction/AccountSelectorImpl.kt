/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.SelectAccountResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.selection.AccountSelectionContext
import ch.nevis.mobile.sdk.api.operation.selection.AccountSelectionHandler
import ch.nevis.mobile.sdk.api.operation.selection.AccountSelector
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [AccountSelector] interface. It stores the account selection step context
 * into its state and resumes the cancellableContinuation found in state with [SelectAccountResponse]
 * indicating that the running operation waiting for an account selection.
 */
class AccountSelectorImpl(
    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : AccountSelector {

    //region AccountSelector
    override fun selectAccount(
        accountSelectionContext: AccountSelectionContext,
        accountSelectionHandler: AccountSelectionHandler
    ) {
        Timber.asTree().sdk("Please select one of the received available accounts!")

        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.accountSelectionHandler = accountSelectionHandler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        val transactionConfirmationData =
            accountSelectionContext.transactionConfirmationData().orElse(null)
        val accounts = accountSelectionContext.accounts()

        if (accounts.isEmpty()) {
            cancellableContinuation.resume(ErrorResponse(BusinessException.accountsNotFound()))
        } else if (transactionConfirmationData == null && accounts.size == 1) {
            accountSelectionHandler.username(
                accountSelectionContext.accounts().first().username()
            )
        } else {
            cancellableContinuation.resume(
                SelectAccountResponse(
                    operationState.operation,
                    accountSelectionContext.accounts(),
                    transactionConfirmationData
                )
            )
        }
    }
    //endregion
}