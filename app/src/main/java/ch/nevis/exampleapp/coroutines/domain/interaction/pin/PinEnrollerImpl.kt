/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction.pin

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.EnrollPinResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.pin.PinEnroller
import ch.nevis.mobile.sdk.api.operation.pin.PinEnrollmentContext
import ch.nevis.mobile.sdk.api.operation.pin.PinEnrollmentHandler
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [PinEnroller] interface. It stores the PIN enrollment step context
 * into its state and resumes the cancellableContinuation found in state with [EnrollPinResponse]
 * indicating that the running operation waiting for a PIN enrollment/creation.
 */
class PinEnrollerImpl(
    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : PinEnroller {

    //region PinEnroller
    override fun enrollPin(
        context: PinEnrollmentContext,
        handler: PinEnrollmentHandler
    ) {
        if (context.lastRecoverableError().isPresent) {
            Timber.asTree().sdk("PIN enrollment failed. Please try again.")
        } else {
            Timber.asTree().sdk("Please start PIN enrollment.")
        }

        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.pinEnrollmentHandler = handler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            EnrollPinResponse(
                context.lastRecoverableError().orElse(null)
            )
        )
    }

    override fun onValidCredentialsProvided() {
        Timber.asTree().sdk("Valid credentials provided during PIN enrollment.")
    }
    //endregion
}
