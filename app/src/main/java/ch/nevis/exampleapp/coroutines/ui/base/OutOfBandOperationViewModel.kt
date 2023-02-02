/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.base

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.usecase.DecodePayloadUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.ProcessOutOfBandPayloadUseCase
import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Abstract, base view model implementation for view models those are related to out-of-band operations.
 * In other words for view models those may start out-of-band payload decoding or processing.
 */
abstract class OutOfBandOperationViewModel : CancellableOperationViewModel() {

    //region Properties
    /**
     * An instance of a [DecodePayloadUseCase] implementation.
     */
    @Inject
    protected lateinit var decodePayloadUseCase: DecodePayloadUseCase

    /**
     * An instance of a [ProcessOutOfBandPayloadUseCase] implementation.
     */
    @Inject
    protected lateinit var processOutOfBandPayloadUseCase: ProcessOutOfBandPayloadUseCase
    //endregion

    //region Public Interface
    /**
     * Starts decoding of out-of-band payload from the given dispatch token response.
     *
     * @param dispatchTokenResponse The dispatch token response to decode out-of-band payload from.
     */
    fun decodeOutOfBandPayload(dispatchTokenResponse: String) {
        viewModelScope.launch(errorHandler) {
            val operationResponse = decodePayloadUseCase.execute(null, dispatchTokenResponse)
            mutableResponseLiveData.postValue(operationResponse)
        }
    }

    /**
     * Starts processing of out-of-band payload.
     *
     * @param payload The [OutOfBandPayload] object to be processed.
     */
    fun processOutOfBandPayload(payload: OutOfBandPayload) {
        viewModelScope.launch(errorHandler) {
            val operationResponse = processOutOfBandPayloadUseCase.execute(payload)
            mutableResponseLiveData.postValue(operationResponse)
        }
    }
    //endregion
}