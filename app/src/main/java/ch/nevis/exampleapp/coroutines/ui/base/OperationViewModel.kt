/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.usecase.DeregisterUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.FinishOperationUseCase
import ch.nevis.exampleapp.coroutines.ui.util.SingleLiveEvent
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Base, abstract view model implementation for view models those deal with operations.
 */
abstract class OperationViewModel : ViewModel() {

    //region Properties
    /**
     * A [LiveData] that can post [Response] objects for fragments those observe it.
     * The [Response] objects created by business logic.
     */
    protected val mutableResponseLiveData = SingleLiveEvent<Response>()
    val responseLiveData: LiveData<Response> = mutableResponseLiveData

    /**
     * An instance of a [FinishOperationUseCase] implementation.
     */
    @Inject
    protected lateinit var finishOperationUseCase: FinishOperationUseCase

    /**
     * An instance of a [DeregisterUseCase] implementation.
     */
    @Inject
    protected lateinit var deregisterUseCase: DeregisterUseCase

    /**
     * Common [CoroutineExceptionHandler] error handler implementation that can be used by the sub-classes of this abstract view model.
     */
    protected val errorHandler = CoroutineExceptionHandler { _, throwable ->
        mutableResponseLiveData.postValue(ErrorResponse(throwable))
    }
    //endregion

    //region Public Interface
    /**
     * Finishes operation with the given operation identifier.
     */
    fun finishOperation() {
        viewModelScope.launch(errorHandler) {
            finishOperationUseCase.execute()
        }
    }

    /**
     * Starts deregistration of a username/account.
     *
     * @param username The username that identifies the account to be deregistered.
     * @param authorizationProvider An [AuthorizationProvider] object that is used for authentication during the deregistration.
     */
    fun deregister(username: String?, authorizationProvider: AuthorizationProvider?) {
        viewModelScope.launch(errorHandler) {
            val response = deregisterUseCase.execute(username, authorizationProvider)
            mutableResponseLiveData.postValue(response)
        }
    }
    //endregion
}