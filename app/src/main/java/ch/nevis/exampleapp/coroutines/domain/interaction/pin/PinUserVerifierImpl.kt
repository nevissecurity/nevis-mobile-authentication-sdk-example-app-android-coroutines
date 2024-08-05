/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction.pin

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyPinResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerificationContext
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerifier
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [PinUserVerifier] interface. It stores the PIN verification step context
 * into its state and resumes the cancellableContinuation found in state with [VerifyPinResponse]
 * indicating that the running operation waiting for a PIN verification.
 */
class PinUserVerifierImpl(
    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : PinUserVerifier {

    //region PinUserVerifier
    override fun verifyPin(
        context: PinUserVerificationContext,
        handler: PinUserVerificationHandler
    ) {
        if (context.lastRecoverableError().isPresent) {
            Timber.asTree().sdk("PIN user verification failed. Please try again.")
        } else {
            Timber.asTree().sdk("Please start PIN user verification.")
        }

        val operationState = stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.pinUserVerificationHandler = handler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            VerifyPinResponse(
                context.lastRecoverableError().orElse(null),
                context.authenticatorProtectionStatus()
            )
        )
    }

    override fun onValidCredentialsProvided() {
        Timber.asTree().sdk("Valid credentials provided during PIN verification.")
    }
    //endregion
}
