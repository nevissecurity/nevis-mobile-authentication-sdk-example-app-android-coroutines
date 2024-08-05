/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyFingerprintResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.domain.util.titleResId
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerificationContext
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerificationHandler
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerifier
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [FingerprintUserVerifier] interface. It stores the fingerprint verification step context
 * into its state and resumes the cancellableContinuation found in state with [VerifyFingerprintResponse]
 * indicating that the running operation waiting for a fingerprint verification.
 *
 * @constructor Creates a new instance.
 * @param stateRepository The state repository that stores the state of the running operation.
 */
class FingerprintUserVerifierImpl(
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : FingerprintUserVerifier {

    //region FingerprintUserVerifier
    /** @suppress */
    override fun verifyFingerprint(
        fingerprintUserVerificationContext: FingerprintUserVerificationContext,
        fingerprintUserVerificationHandler: FingerprintUserVerificationHandler
    ) {
        if (fingerprintUserVerificationContext.lastRecoverableError().isPresent) {
            Timber.asTree().sdk("Fingerprint user verification failed. Please try again.")
        } else {
            Timber.asTree().sdk("Please start fingerprint user verification.")
        }

        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        operationState.fingerprintUserVerificationHandler = fingerprintUserVerificationHandler

        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()

        cancellableContinuation.resume(
            VerifyFingerprintResponse(
                fingerprintUserVerificationContext.authenticator().titleResId(),
                fingerprintUserVerificationContext.lastRecoverableError().orElse(null)
            )
        )
    }

    /** @suppress */
    override fun onValidCredentialsProvided() {
        Timber.asTree()
            .sdk("Valid credentials provided during fingerprint verification.")
    }
    //endregion
}
