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
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricPromptOptions
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [VerifyBiometricUseCase] interface.
 *
 * @constructor Creates a new instance.
 * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
 *  a [UserInteractionOperationState].
 */
class VerifyBiometricUseCaseImpl(
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : VerifyBiometricUseCase {

    //region VerifyBiometricUseCase
    override suspend fun execute(biometricPromptOptions: BiometricPromptOptions): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState =
                stateRepository.get() ?: throw BusinessException.invalidState()
            val biometricUserVerificationHandler = operationState.biometricUserVerificationHandler
                ?: throw BusinessException.invalidState()
            operationState.cancellableContinuation = cancellableContinuation
            operationState.biometricUserVerificationHandler = null
            biometricUserVerificationHandler.listenForOsCredentials(biometricPromptOptions)
        }
    }
    //endregion
}
