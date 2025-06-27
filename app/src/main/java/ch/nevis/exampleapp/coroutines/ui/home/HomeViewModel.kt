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
import ch.nevis.exampleapp.coroutines.domain.usecase.GetFidoUafAttestationInformationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.InitializeClientUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.MetaDataUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.StartChangePasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.StartChangePinUseCase
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
 *
 * @constructor Creates a new instance.
 * @param configurationProvider An instance of a [ConfigurationProvider] implementation.
 * @param initializeClientUseCase An instance of a [InitializeClientUseCase] implementation.
 * @param getAccountsUseCase An instance of a [GetAccountsUseCase] implementation.
 * @param getAuthenticatorsUseCase An instance of a [GetAuthenticatorsUseCase] implementation.
 * @param getFidoUafAttestationInformationUseCase An instance of a [GetFidoUafAttestationInformationUseCase] implementation.
 * @param metaDataUseCase An instance of a [MetaDataUseCase] implementation.
 * @param startChangePinUseCase An instance of a [StartChangePinUseCase] implementation.
 * @param startChangePasswordUseCase An instance of a [StartChangePasswordUseCase] implementation.
 * @param deleteAuthenticatorsUseCase An instance of a [DeleteAuthenticatorsUseCase] implementation.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val configurationProvider: ConfigurationProvider,
    private val initializeClientUseCase: InitializeClientUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getAuthenticatorsUseCase: GetAuthenticatorsUseCase,
    private val getFidoUafAttestationInformationUseCase: GetFidoUafAttestationInformationUseCase,
    private val metaDataUseCase: MetaDataUseCase,
    private val startChangePinUseCase: StartChangePinUseCase,
    private val startChangePasswordUseCase: StartChangePasswordUseCase,
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
     * Retrieves the list of registered accounts.
     */
    fun getAccounts() {
        viewModelScope.launch(errorHandler) {
            val response = getAccountsUseCase.execute()
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Retrieves the meta data of Nevis Mobile Authentication SDK.
     */
    fun getMetaData() {
        viewModelScope.launch(errorHandler) {
            val response = metaDataUseCase.execute()
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Retrieves the FIDO UAF attestation information.
     */
    fun getAttestationInformation() {
        viewModelScope.launch(errorHandler) {
            val response = getFidoUafAttestationInformationUseCase.execute()
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
     * Starts a change credential operation.
     *
     * @param authenticatorType The AAID of the current authenticator.
     */
    fun changeCredential(authenticatorType: String) {
        viewModelScope.launch(errorHandler) {
            var response: Response? = getAuthenticatorsUseCase.execute()

            if (response is GetAuthenticatorsResponse) {
                var accounts: Set<Account>? = null
                response.authenticators.forEach {
                    if (it.aaid() == authenticatorType) {
                        accounts = it.registration().registeredAccounts()
                    }
                }

                response = accounts?.let {
                    when (it.size) {
                        0 -> ErrorResponse(BusinessException.accountsNotFound())
                        1 -> when (authenticatorType) {
                            Authenticator.PIN_AUTHENTICATOR_AAID ->
                                startChangePinUseCase.execute(it.first().username())
                            Authenticator.PASSWORD_AUTHENTICATOR_AAID ->
                                startChangePasswordUseCase.execute(it.first().username())
                            else -> ErrorResponse(BusinessException.invalidState())
                        }
                        else -> when (authenticatorType) {
                            Authenticator.PIN_AUTHENTICATOR_AAID ->
                                SelectAccountResponse(Operation.CHANGE_PIN, it)
                            Authenticator.PASSWORD_AUTHENTICATOR_AAID ->
                                SelectAccountResponse(Operation.CHANGE_PASSWORD, it)
                            else -> ErrorResponse(BusinessException.invalidState())
                        }
                    }
                }
            }
            mutableResponseLiveData.postValue(response)
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
