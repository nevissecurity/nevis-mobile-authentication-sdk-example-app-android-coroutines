/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [VerifyFingerprintUseCase] interface.
 */
class VerifyFingerprintUseCaseImpl(
    /**
     * An instance of an [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : VerifyFingerprintUseCase {

    //region VerifyFingerprintUseCase
    override suspend fun execute(): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState =
                stateRepository.get() ?: throw BusinessException.invalidState()
            val fingerprintUserVerificationHandler =
                operationState.fingerprintUserVerificationHandler
                    ?: throw BusinessException.invalidState()
            operationState.cancellableContinuation = cancellableContinuation
            operationState.fingerprintUserVerificationHandler = null
            operationState.osAuthenticationListenHandler =
                fingerprintUserVerificationHandler.listenForOsCredentials()
        }
    }
    //endregion
}
