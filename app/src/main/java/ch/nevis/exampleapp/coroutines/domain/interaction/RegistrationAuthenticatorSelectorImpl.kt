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
import ch.nevis.mobile.sdk.api.localdata.Authenticator
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionContext
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelectionHandler
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Registration specific implementation of [AuthenticatorSelector] interface. It stores the authenticator
 * selection step context into its state and resumes the cancellableContinuation found in state with
 * [SelectAuthenticatorResponse] indicating that the running operation waiting for an authenticator selection.
 */
class RegistrationAuthenticatorSelectorImpl(
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

        val authenticators = authenticatorSelectionContext.authenticators().filter {
            isAvailableForRegistration(it, authenticatorSelectionContext)
        }.toSet()

        val operationState = stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.username = authenticatorSelectionContext.account().username()
        operationState.authenticatorSelectionHandler = authenticatorSelectionHandler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            SelectAuthenticatorResponse(
                authenticators
            )
        )
    }
    //endregion

    //region Private Interface
    private fun isAvailableForRegistration(
        authenticator: Authenticator,
        context: AuthenticatorSelectionContext
    ): Boolean {
        val username = context.account().username()
        val authenticators = context.authenticators()

        val biometricRegistered = authenticators.any {
            it.aaid() == Authenticator.BIOMETRIC_AUTHENTICATOR_AAID &&
                    it.registration().isRegistered(username)
        }

        val canRegisterBiometric = authenticators.any {
            it.aaid() == Authenticator.BIOMETRIC_AUTHENTICATOR_AAID &&
                    context.isPolicyCompliant(it.aaid())
        }

        val canRegisterFingerprint = authenticators.any {
            it.aaid() == Authenticator.FINGERPRINT_AUTHENTICATOR_AAID &&
                    context.isPolicyCompliant(it.aaid())
        }

        // If biometric can be registered (or is already registered), or if we cannot
        // register fingerprint, do not propose to register fingerprint (we favor biometric over fingerprint).
        if ((canRegisterBiometric || biometricRegistered || !canRegisterFingerprint) &&
            authenticator.aaid() == Authenticator.FINGERPRINT_AUTHENTICATOR_AAID
        ) {
            return false
        }

        // Do not display policy non-compliant authenticators (this includes already registered
        // authenticators), nor those not supported by hardware.
        return authenticator.isSupportedByHardware && context.isPolicyCompliant(authenticator.aaid())
    }
    //endregion
}