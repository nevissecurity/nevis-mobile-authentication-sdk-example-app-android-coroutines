/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.common.configuration.ConfigurationProvider
import ch.nevis.exampleapp.coroutines.common.settings.Settings
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.SelectAuthenticatorResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.domain.util.isUserEnrolled
import ch.nevis.exampleapp.coroutines.domain.util.titleResId
import ch.nevis.exampleapp.coroutines.domain.validation.AuthenticatorValidator
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model.AuthenticatorItem
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionContext
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionHandler
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Supported operations during authenticator selection.
 */
enum class AuthenticatorSelectorOperation {

    /**
     * Registration operation.
     */
    REGISTRATION,

    /**
     * Authentication operation.
     */
    AUTHENTICATION,
}

/**
 * Default implementation of [AuthenticatorSelector] interface.
 */
class AuthenticatorSelectorImpl(
    /**
     * An instance of a [ConfigurationProvider] implementation.
     */
    private val configurationProvider: ConfigurationProvider,

    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>,

    /**
     * An instance of an [AuthenticatorValidator] interface implementation.
     */
    private val authenticatorValidator: AuthenticatorValidator,

    /**
     * An instance of a [Settings] interface implementation.
     */
    private val settings: Settings,

    /**
     * The current operation.
     */
    private val operation: AuthenticatorSelectorOperation
) : AuthenticatorSelector {
    override fun selectAuthenticator(
        context: AuthenticatorSelectionContext,
        handler: AuthenticatorSelectionHandler
    ) {
        Timber.asTree().sdk("Please select one of the received available authenticators!")

        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        val authenticators = when (operation) {
            AuthenticatorSelectorOperation.REGISTRATION -> {
                authenticatorValidator.validateForRegistration(
                    context,
                    configurationProvider.authenticatorAllowlist
                )
            }
            AuthenticatorSelectorOperation.AUTHENTICATION -> {
                authenticatorValidator.validateForAuthentication(
                    context,
                    configurationProvider.authenticatorAllowlist
                )
            }
        }

        if (authenticators.isEmpty()) {
            Timber.e("No available authenticators found. Cancelling authenticator selection.")
            return handler.cancel()
        }

        val authenticatorItems = authenticators.map {
            AuthenticatorItem(
                it.aaid(),
                context.isPolicyCompliant(it.aaid()),
                it.isUserEnrolled(
                    context.account().username(),
                    settings.allowClass2Sensors
                ),
                it.titleResId()
            )
        }.toSet()

        operationState.username = context.account().username()
        operationState.authenticatorSelectionHandler = handler

        cancellableContinuation.resume(
            SelectAuthenticatorResponse(authenticatorItems)
        )
    }
}
