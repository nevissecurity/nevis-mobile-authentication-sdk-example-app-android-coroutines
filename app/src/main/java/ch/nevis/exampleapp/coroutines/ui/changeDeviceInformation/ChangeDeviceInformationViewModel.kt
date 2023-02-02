/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.changeDeviceInformation

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangeDeviceInformationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.GetDeviceInformationUseCase
import ch.nevis.exampleapp.coroutines.ui.base.OperationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model implementation of Change Device Information view.
 */
@HiltViewModel
class ChangeDeviceInformationViewModel @Inject constructor(
    /**
     * An instance of a [GetDeviceInformationUseCase] implementation.
     */
    private val getDeviceInformationUseCase: GetDeviceInformationUseCase,

    /**
     * An instance of a [ChangeDeviceInformationUseCase] implementation.
     */
    private val changeDeviceInformationUseCase: ChangeDeviceInformationUseCase
) : OperationViewModel() {

    //region Public Interface
    /**
     * Gets the current device information stored by the client.
     */
    fun getDeviceInformation() {
        viewModelScope.launch(errorHandler) {
            val response = getDeviceInformationUseCase.execute()
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Starts change device information operation.
     *
     * @param name The new name of the device information.
     */
    fun changeDeviceInformation(name: String) {
        if (name.isBlank()) {
            return
        }

        viewModelScope.launch(errorHandler) {
            val response = changeDeviceInformationUseCase.execute(name)
            mutableResponseLiveData.postValue(response)
        }
    }
    //endregion
}