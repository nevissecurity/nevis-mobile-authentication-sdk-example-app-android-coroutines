/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.common.settings.Settings
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.SelectAuthenticatorResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.domain.util.isUserEnrolled
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model.AuthenticatorItem
import ch.nevis.mobile.sdk.api.localdata.Authenticator
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
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>,

    /**
     * An instance of a [Settings] interface implementation.
     */
    private val settings: Settings
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

        val authenticatorItems = authenticatorSelectionContext.authenticators().mapNotNull {
            mapForAuthentication(it, authenticatorSelectionContext)
        }.toSet()

        operationState.username = authenticatorSelectionContext.account().username()
        operationState.authenticatorSelectionHandler = authenticatorSelectionHandler

        cancellableContinuation.resume(
            SelectAuthenticatorResponse(authenticatorItems)
        )
    }
    //endregion

    //region Private Interface
    private fun mapForAuthentication(
        authenticator: Authenticator,
        context: AuthenticatorSelectionContext
    ): AuthenticatorItem? {
        Timber.d("Checking if authenticator %s is eligible for authentication.", authenticator.aaid())
        return if (authenticator.registration()
                .isRegistered(context.account().username()) && authenticator.isSupportedByHardware
        ) {
            AuthenticatorItem(
                authenticator.aaid(),
                context.isPolicyCompliant(authenticator.aaid()),
                authenticator.isUserEnrolled(
                    context.account().username(),
                    settings.allowClass2Sensors
                )
            )
        } else {
            null
        }
    }
    //endregion
}