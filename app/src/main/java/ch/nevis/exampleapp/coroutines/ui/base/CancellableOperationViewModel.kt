/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022-2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.base

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.usecase.CancelOperationUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Abstract, base view model implementation for view models those are related to cancellable operations.
 *
 * An operation may consist multiple steps e.g.: account selection, authenticator selection, user verification.
 * In this example application all steps are implemented as a separated view and each view has a view model
 * implementation. If a view and its view model are related to an operation step in which the operation can
 * be cancelled, then the view model of the actual operation step view must be a sub-class of this abstract
 * view model.
 */
abstract class CancellableOperationViewModel : OperationViewModel() {

    //region Properties
    /**
     * An instance of a [CancelOperationUseCase] implementation.
     */
    @Inject
    lateinit var cancelOperationUseCase: CancelOperationUseCase
    //endregion

    //region Public Interface
    /**
     * Cancels the current operation.
     */
    fun cancelOperation() {
        viewModelScope.launch(errorHandler) {
            val response = cancelOperationUseCase.execute()
            mutableResponseLiveData.postValue(response)
        }
    }
    //endregion
}
