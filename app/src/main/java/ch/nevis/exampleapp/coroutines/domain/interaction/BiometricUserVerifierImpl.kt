/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyBiometricResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerificationContext
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerifier
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [BiometricUserVerifier] interface. It stores the biometric verification step context
 * into its state and resumes the cancellableContinuation found in state with [VerifyBiometricResponse]
 * indicating that the running operation waiting for a biometric verification.
 */
class BiometricUserVerifierImpl(
    /**
     * The state repository that stores the state of the running operation.
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : BiometricUserVerifier {

    //region BiometricUserVerifier
    override fun verifyBiometric(
        biometricUserVerificationContext: BiometricUserVerificationContext?,
        biometricUserVerificationHandler: BiometricUserVerificationHandler?
    ) {
        Timber.asTree().sdk("Please start biometric user verification.")

        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.biometricUserVerificationHandler = biometricUserVerificationHandler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            VerifyBiometricResponse()
        )
    }

    override fun onValidCredentialsProvided() {
        Timber.asTree()
            .sdk("The user successfully verified herself/himself with biometric authenticator.")
    }
    //endregion
}