/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAccount

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.dagger.ApplicationModule
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.usecase.*
import ch.nevis.exampleapp.coroutines.ui.base.CancellableOperationViewModel
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
     * @param username The username assigned to the selected account.
     */
    fun selectAccount(operation: Operation, username: String) {
        viewModelScope.launch(errorHandler) {
            when (operation) {
                Operation.AUTHENTICATION -> inBandAuthentication(username)
                Operation.DEREGISTRATION -> inBandAuthenticationForDeregistration(username)
                Operation.CHANGE_PIN -> changePin(username)
                Operation.CHANGE_PASSWORD -> changePassword(username)
                Operation.OUT_OF_BAND_AUTHENTICATION -> outOfBandAuthentication(username)
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
     * Starts an in-band authentication.
     *
     * @param username The username that identifies the account the in-band authentication must be started for.
     */
    private suspend fun inBandAuthentication(username: String) {
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
