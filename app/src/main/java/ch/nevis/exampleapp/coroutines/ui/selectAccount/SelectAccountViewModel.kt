/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022-2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAccount

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.dagger.ApplicationModule
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.usecase.InBandAuthenticationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.SelectAccountUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.StartChangePasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.StartChangePinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.TransactionConfirmationUseCase
import ch.nevis.exampleapp.coroutines.ui.base.CancellableOperationViewModel
import ch.nevis.mobile.sdk.api.localdata.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * View model implementation for Select Account view.
 *
 * @constructor Creates a new instance.
 * @param startChangePinUseCase An instance of a [StartChangePinUseCase] implementation.
 * @param startChangePasswordUseCase An instance of a [StartChangePasswordUseCase] implementation.
 * @param inBandAuthenticationUseCase An instance of an [InBandAuthenticationUseCase] implementation.
 * @param inBandAuthenticationForDeregistrationUseCase An instance of a [InBandAuthenticationUseCase]
 *  implementation for the cases deregistration must be started right after a successful in-band authentication.
 * @param selectAccountUseCase An instance of a [SelectAccountUseCase] implementation.
 */
@HiltViewModel
class SelectAccountViewModel @Inject constructor(
    private val startChangePinUseCase: StartChangePinUseCase,
    private val startChangePasswordUseCase: StartChangePasswordUseCase,
    private val transactionConfirmationUseCase: TransactionConfirmationUseCase,
    @Named(ApplicationModule.IN_BAND_AUTHENTICATION_USE_CASE_DEFAULT)
    private val inBandAuthenticationUseCase: InBandAuthenticationUseCase,
    @Named(ApplicationModule.IN_BAND_AUTHENTICATION_USE_CASE_FOR_DEREGISTRATION)
    private val inBandAuthenticationForDeregistrationUseCase: InBandAuthenticationUseCase,
    private val selectAccountUseCase: SelectAccountUseCase
) : CancellableOperationViewModel() {

    //region Public Interface
    /**
     * Selects account for the given operation and user. The supported operations are
     * [Operation.AUTHENTICATION], [Operation.DEREGISTRATION], [Operation.CHANGE_PIN],
     * [Operation.CHANGE_PASSWORD] and [Operation.OUT_OF_BAND_AUTHENTICATION].
     *
     * @param operation The operation the account selected for.
     * @param account The selected account.
     * @param transactionConfirmationMessage The transaction confirmation message
     * that need to be confirmed or cancelled by the user.
     */
    fun selectAccount(operation: Operation, account: Account, transactionConfirmationMessage: String?) {
        viewModelScope.launch(errorHandler) {
            if (transactionConfirmationMessage != null) {
                // Transaction confirmation data is received from the SDK
                // Show it to the user for confirmation or cancellation
                // The AccountSelectionHandler will be invoked or cancelled there.
                confirm(transactionConfirmationMessage, account)
            } else when (operation) {
                Operation.AUTHENTICATION -> inBandAuthenticate(account.username())
                Operation.DEREGISTRATION -> inBandAuthenticationForDeregistration(account.username())
                Operation.CHANGE_PIN -> changePin(account.username())
                Operation.CHANGE_PASSWORD -> changePassword(account.username())
                Operation.OUT_OF_BAND_AUTHENTICATION -> outOfBandAuthentication(account.username())
                else -> throw BusinessException.invalidState()
            }
        }
    }
    //endregion

    //region Private interface
    /**
     * Starts PIN change.
     *
     * @param username The username that identifies the account the PIN change must be started for.
     */
    private suspend fun changePin(username: String) {
        val response = startChangePinUseCase.execute(username)
        mutableResponseLiveData.postValue(response)
    }

    /**
     * Starts password change.
     *
     * @param username The username that identifies the account the Password change must be started for.
     */
    private suspend fun changePassword(username: String) {
        val response = startChangePasswordUseCase.execute(username)
        mutableResponseLiveData.postValue(response)
    }

    /**
     * Confirms the transaction.
     *
     * @param message: The transaction confirmation message that need to be confirmed or cancelled by the user.
     * @param account: The current account.
     */
    private suspend fun confirm(message: String, account: Account) {
        val response = transactionConfirmationUseCase.execute(account, message)
        mutableResponseLiveData.postValue(response)
    }

    /**
     * Starts an in-band authentication.
     *
     * @param username The username that identifies the account the in-band authentication must be started for.
     */
    private suspend fun inBandAuthenticate(username: String) {
        val response = inBandAuthenticationUseCase.execute(username)
        mutableResponseLiveData.postValue(response)
    }

    /**
     * Starts an in-band authentication for an identity suite de-registration.
     *
     * @param username The username that identifies the account the de-registration must be started for.
     */
    private suspend fun inBandAuthenticationForDeregistration(username: String) {
        val response = inBandAuthenticationForDeregistrationUseCase.execute(username)
        mutableResponseLiveData.postValue(response)
    }

    /**
     * Continues an out-of-band authentication.
     *
     * @param username The username that identifies the account the out-of-band authentication must be started for.
     */
    private suspend fun outOfBandAuthentication(username: String) {
        val response = selectAccountUseCase.execute(username)
        mutableResponseLiveData.postValue(response)
    }
    //endregion
}
