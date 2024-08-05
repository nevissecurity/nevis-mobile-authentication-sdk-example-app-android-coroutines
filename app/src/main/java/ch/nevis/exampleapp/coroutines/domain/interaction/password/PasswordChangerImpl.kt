/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction.password

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.ChangePasswordResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePasswordOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.password.PasswordChangeContext
import ch.nevis.mobile.sdk.api.operation.password.PasswordChangeHandler
import ch.nevis.mobile.sdk.api.operation.password.PasswordChanger
import ch.nevis.mobile.sdk.api.operation.password.PasswordPolicy
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [PasswordChanger] interface. It stores the password change step context
 * into its state and resumes the cancellableContinuation found in state with [ChangePasswordResponse]
 * indicating that the running operation waiting for a new password.
 *
 * @constructor Creates a new instance.
 * @param policy An instance of a [PasswordPolicy] interface implementation.
 * @param stateRepository The state repository that stores the state of the running operation.
 */
class PasswordChangerImpl(
    private val policy: PasswordPolicy,
    private val stateRepository: OperationStateRepository<ChangePasswordOperationState>
) : PasswordChanger {

    //region PasswordChanger
    /** @suppress */
    override fun changePassword(
        context: PasswordChangeContext,
        handler: PasswordChangeHandler
    ) {
        if (context.lastRecoverableError().isPresent) {
            Timber.asTree().sdk("Password change failed. Please try again.")
        } else {
            Timber.asTree().sdk("Please start Password change.")
        }

        val operationState = stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.passwordChangeHandler = handler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            ChangePasswordResponse(
                context.authenticatorProtectionStatus(),
                context.lastRecoverableError().orElse(null)
            )
        )
    }

    //  You can add custom password policy by overriding the `passwordPolicy` getter
    /** @suppress */
    override fun passwordPolicy(): PasswordPolicy = policy
    //endregion
}
