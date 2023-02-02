/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.ChangePinResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePinOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.pin.PinChangeContext
import ch.nevis.mobile.sdk.api.operation.pin.PinChangeHandler
import ch.nevis.mobile.sdk.api.operation.pin.PinChanger
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [PinChanger] interface. It stores the PIN change step context
 * into its state and resumes the cancellableContinuation found in state with [ChangePinResponse]
 * indicating that the running operation waiting for a new PIN.
 */
class PinChangerImpl(
    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<ChangePinOperationState>
) : PinChanger {

    //region PinChanger
    override fun changePin(pinChangeContext: PinChangeContext, pinChangeHandler: PinChangeHandler) {
        if (pinChangeContext.lastRecoverableError().isPresent) {
            Timber.asTree().sdk("PIN change failed. Please try again.")
        } else {
            Timber.asTree().sdk("Please start PIN change.")
        }

        val operationState = stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.pinChangeHandler = pinChangeHandler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            ChangePinResponse(
                pinChangeContext.authenticatorProtectionStatus(),
                pinChangeContext.lastRecoverableError().orElse(null)
            )
        )
    }
    //endregion
}