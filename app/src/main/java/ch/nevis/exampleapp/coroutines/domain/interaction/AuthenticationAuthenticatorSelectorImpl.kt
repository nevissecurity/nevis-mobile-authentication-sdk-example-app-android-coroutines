/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.SelectAuthenticatorResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionContext
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionHandler
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Authentication specific implementation of [AuthenticatorSelector] interface. It stores the authenticator
 * selection step context into its state and resumes the cancellableContinuation found in state with
 * [SelectAuthenticatorResponse] indicating that the running operation waiting for an authenticator selection.
 */
class AuthenticationAuthenticatorSelectorImpl(
    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : AuthenticatorSelector {

    //region AuthenticatorSelector
    override fun selectAuthenticator(
        authenticatorSelectionContext: AuthenticatorSelectionContext,
        authenticatorSelectionHandler: AuthenticatorSelectionHandler
    ) {
        Timber.asTree().sdk("Please select one of the received available authenticators!")

        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        val authenticators = authenticatorSelectionContext.authenticators().filter {
            it.registration().isRegistered(authenticatorSelectionContext.account().username()) &&
                    it.isSupportedByHardware
        }.toSet()

        operationState.username = authenticatorSelectionContext.account().username()
        operationState.authenticatorSelectionHandler = authenticatorSelectionHandler

        cancellableContinuation.resume(
            SelectAuthenticatorResponse(authenticators)
        )
    }
    //endregion
}