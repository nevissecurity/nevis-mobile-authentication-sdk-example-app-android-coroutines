/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction.password

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.EnrollPasswordResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.password.PasswordEnroller
import ch.nevis.mobile.sdk.api.operation.password.PasswordEnrollmentContext
import ch.nevis.mobile.sdk.api.operation.password.PasswordEnrollmentHandler
import ch.nevis.mobile.sdk.api.operation.password.PasswordPolicy
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [PasswordEnroller] interface. Navigates to Credential view with the
 * received [PasswordEnrollmentHandler] and [ch.nevis.mobile.sdk.api.operation.password.PasswordEnrollmentError]
 * objects.
 *
 * Default implementation of [PasswordEnroller] interface. It stores the password enrollment step context
 * into its state and resumes the cancellableContinuation found in state with [EnrollPasswordResponse]
 * indicating that the running operation waiting for a password enrollment/creation.
 *
 * @constructor Creates a new instance.
 * @param policy An instance of a [PasswordPolicy] interface implementation.
 * @param stateRepository The state repository that stores the state of the running operation.
 */
class PasswordEnrollerImpl(
    private val policy: PasswordPolicy,
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : PasswordEnroller {

    //region PasswordEnroller
    /** @suppress */
    override fun enrollPassword(
        context: PasswordEnrollmentContext,
        handler: PasswordEnrollmentHandler
    ) {
        if (context.lastRecoverableError().isPresent) {
            Timber.asTree().sdk("Password enrollment failed. Please try again.")
        } else {
            Timber.asTree().sdk("Please start Password enrollment.")
        }

        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.passwordEnrollmentHandler = handler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            EnrollPasswordResponse(
                context.lastRecoverableError().orElse(null)
            )
        )
    }

    /** @suppress */
    override fun onValidCredentialsProvided() {
        Timber.asTree().sdk("Valid credentials provided during Password enrollment.")
    }

    //  You can add custom password policy by overriding the `passwordPolicy` getter
    /** @suppress */
    override fun passwordPolicy(): PasswordPolicy = policy
    //endregion
}
