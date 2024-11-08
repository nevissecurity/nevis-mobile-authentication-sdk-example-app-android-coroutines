/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022-2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.transactionConfirmation

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.usecase.SelectAccountUseCase
import ch.nevis.exampleapp.coroutines.ui.base.CancellableOperationViewModel
import ch.nevis.mobile.sdk.api.localdata.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model implementation of Transaction Confirmation view.
 *
 * @constructor Creates a new instance.
 * @param selectAccountUseCase An instance of a [SelectAccountUseCase] implementation.
 */
@HiltViewModel
class TransactionConfirmationViewModel @Inject constructor(
    private val selectAccountUseCase: SelectAccountUseCase
) : CancellableOperationViewModel() {

    //region Public Interface
    /**
     * Confirms the transaction, the operation will be continued.
     *
     * @param account The previously selected account.
     */
    fun confirm(account: Account?) {
        account ?: throw BusinessException.invalidState()

        viewModelScope.launch(errorHandler) {
            val response = selectAccountUseCase.execute(account.username())
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Denies the transaction, the operation will be cancelled.
     */
    fun deny() {
        cancelOperation()
    }
    //endregion
}
