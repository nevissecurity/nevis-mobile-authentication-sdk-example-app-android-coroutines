/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.usecase.SelectAuthenticatorUseCase
import ch.nevis.exampleapp.coroutines.ui.base.CancellableOperationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model implementation for Select Authenticator view.
 */
@HiltViewModel
class SelectAuthenticatorViewModel @Inject constructor(
    /**
     * An instance of a [SelectAuthenticatorUseCase] implementation.
     */
    private val selectAuthenticatorUseCase: SelectAuthenticatorUseCase
) : CancellableOperationViewModel() {

    //region Public Interface
    /**
     * Notifies the requester operation about the selection.
     *
     * @param aaid The AAID of the selected authenticator.
     */
    fun selectAuthenticator(aaid: String) {
        viewModelScope.launch(errorHandler) {
            val operationResponse = selectAuthenticatorUseCase.execute(aaid)
            mutableResponseLiveData.postValue(operationResponse)
        }
    }
    //endregion
}