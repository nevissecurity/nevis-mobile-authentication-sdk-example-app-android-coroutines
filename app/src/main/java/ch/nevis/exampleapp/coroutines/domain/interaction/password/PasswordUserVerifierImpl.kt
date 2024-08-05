/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction.password

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyPasswordResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyPinResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.userverification.PasswordUserVerificationContext
import ch.nevis.mobile.sdk.api.operation.userverification.PasswordUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.PasswordUserVerifier
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [PasswordUserVerifier] interface. It stores the password verification step context
 * into its state and resumes the cancellableContinuation found in state with [VerifyPinResponse]
 * indicating that the running operation waiting for a password verification.
 *
 * @constructor Creates a new instance.
 * @param stateRepository The state repository that stores the state of the running operation.
 */
class PasswordUserVerifierImpl(
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : PasswordUserVerifier {

    //region PasswordUserVerifier
    /** @suppress */
    override fun verifyPassword(
        context: PasswordUserVerificationContext,
        handler: PasswordUserVerificationHandler
    ) {
        if (context.lastRecoverableError().isPresent) {
            Timber.asTree().sdk("Password user verification failed. Please try again.")
        } else {
            Timber.asTree().sdk("Please start Password user verification.")
        }

        val operationState = stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.passwordUserVerificationHandler = handler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            VerifyPasswordResponse(
                context.lastRecoverableError().orElse(null),
                context.authenticatorProtectionStatus()
            )
        )
    }

    /** @suppress */
    override fun onValidCredentialsProvided() {
        Timber.asTree().sdk("Valid credentials provided during Password verification.")
    }
    //endregion
}
