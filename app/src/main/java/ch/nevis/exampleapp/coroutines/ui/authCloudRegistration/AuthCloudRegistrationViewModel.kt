/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.authCloudRegistration

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.usecase.AuthCloudApiRegistrationUseCase
import ch.nevis.exampleapp.coroutines.ui.base.OperationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model implementation of Auth Cloud API Registration view.
 */
@HiltViewModel
class AuthCloudRegistrationViewModel @Inject constructor(
    /**
     * An instance of a [AuthCloudApiRegistrationUseCase] implementation.
     */
    private val authCloudApiRegistrationUseCase: AuthCloudApiRegistrationUseCase
) : OperationViewModel() {

    //region Public Interface
    /**
     * Starts Auth Cloud API registration operation.
     *
     * @param enrollResponse The response of the Cloud HTTP API to the enroll endpoint.
     * @param appLinkUri The value of the `appLinkUri` attribute in the enroll response sent by the server.
     */
    fun authCloudRegistration(enrollResponse: String, appLinkUri: String) {
        val optionalEnrollResponse = enrollResponse.ifBlank {
            null
        }

        val optionalAppLinkUri = appLinkUri.ifBlank {
            null
        }

        viewModelScope.launch(errorHandler) {
            val response =
                authCloudApiRegistrationUseCase.execute(optionalEnrollResponse, optionalAppLinkUri)
            mutableResponseLiveData.postValue(response)
        }
    }
    //endregion
}