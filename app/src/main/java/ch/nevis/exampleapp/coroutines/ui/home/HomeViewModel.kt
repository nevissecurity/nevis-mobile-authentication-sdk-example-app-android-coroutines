/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.home

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.common.configuration.ConfigurationProvider
import ch.nevis.exampleapp.coroutines.common.configuration.Environment
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.*
import ch.nevis.exampleapp.coroutines.domain.usecase.DeleteAuthenticatorsUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.GetAccountsUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.GetAuthenticatorsUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.InitializeClientUseCase
import ch.nevis.exampleapp.coroutines.ui.base.OutOfBandOperationViewModel
import ch.nevis.mobile.sdk.api.localdata.Account
import ch.nevis.mobile.sdk.api.localdata.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * View model implementation of Home view.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    /**
     * An instance of a [ConfigurationProvider] implementation.
     */
    private val configurationProvider: ConfigurationProvider,

    /**
     * An instance of a [InitializeClientUseCase] implementation.
     */
    private val initializeClientUseCase: InitializeClientUseCase,

    /**
     * An instance of a [GetAccountsUseCase] implementation.
     */
    private val getAccountsUseCase: GetAccountsUseCase,

    /**
     * An instance of a [GetAuthenticatorsUseCase] implementation.
     */
    private val getAuthenticatorsUseCase: GetAuthenticatorsUseCase,

    /**
     * An instance of a [DeleteAuthenticatorsUseCase] implementation.
     */
    private val deleteAuthenticatorsUseCase: DeleteAuthenticatorsUseCase,
) : OutOfBandOperationViewModel() {

    //region Public Interface
    /**
     * Starts initialization of [ch.nevis.mobile.sdk.api.MobileAuthenticationClient].
     */
    fun initClient() {
        viewModelScope.launch(errorHandler) {
            val operationResponse =
                initializeClientUseCase.execute(configurationProvider.configuration)
            mutableResponseLiveData.postValue(operationResponse)
        }
    }

    /**
     * Starts initialization of Home view. This method should be called after a successful client
     * initialization.
     */
    fun initView() {
        viewModelScope.launch(errorHandler) {
            val response = getAccountsUseCase.execute()
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Starts an in-band authentication operation.
     */
    fun inBandAuthentication() {
        viewModelScope.launch(errorHandler) {
            var response = getAccountsUseCase.execute()
            if (response is GetAccountsResponse) {
                response = if (response.accounts.isNotEmpty()) {
                    SelectAccountResponse(
                        Operation.AUTHENTICATION,
                        response.accounts
                    )
                } else {
                    ErrorResponse(BusinessException.accountsNotFound())
                }
            }
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Starts a change PIN operation.
     */
    fun changePin() {
        viewModelScope.launch(errorHandler) {
            var response: Response? = getAuthenticatorsUseCase.execute()

            if (response is GetAuthenticatorsResponse) {
                var accounts: Set<Account>? = null
                response.authenticators.forEach {
                    if (it.aaid() == Authenticator.PIN_AUTHENTICATOR_AAID) {
                        accounts = it.registration().registeredAccounts()
                    }
                }

                response = accounts?.let {
                    if (it.isNotEmpty()) {
                        SelectAccountResponse(
                            Operation.CHANGE_PIN,
                            it
                        )
                    } else {
                        null
                    }
                }
            }

            mutableResponseLiveData.postValue(
                response ?: ErrorResponse(BusinessException.accountsNotFound())
            )
        }
    }

    /**
     * Starts de-registration operation.
     */
    fun deregister() {
        viewModelScope.launch(errorHandler) {
            when (configurationProvider.environment) {
                Environment.AUTHENTICATION_CLOUD -> {
                    val operationResponse = deregisterUseCase.execute()
                    mutableResponseLiveData.postValue(operationResponse)
                }
                Environment.IDENTITY_SUITE -> {
                    // In this case
                    var response = getAccountsUseCase.execute()
                    if (response is GetAccountsResponse) {
                        response = if (response.accounts.isNotEmpty()) {
                            SelectAccountResponse(
                                Operation.DEREGISTRATION,
                                response.accounts
                            )
                        } else {
                            ErrorResponse(BusinessException.accountsNotFound())
                        }
                    }
                    mutableResponseLiveData.postValue(response)
                }
            }
        }
    }

    /**
     * Starts deleting local authenticators of all registered users.
     */
    fun deleteAuthenticators() {
        viewModelScope.launch(errorHandler) {
            var response = getAccountsUseCase.execute()
            if (response is GetAccountsResponse) {
                val accounts = response.accounts
                response = if (accounts.isNotEmpty()) {
                    withContext(Dispatchers.IO) {
                        deleteAuthenticatorsUseCase.execute(accounts)
                    }
                } else {
                    ErrorResponse(BusinessException.accountsNotFound())
                }
            }
            mutableResponseLiveData.postValue(response)
        }
    }
    //endregion
}