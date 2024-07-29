/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodePromptOptions
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [VerifyDevicePasscodeUseCase] interface.
 */
class VerifyDevicePasscodeUseCaseImpl(
    /**
     * An instance of an [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>
) : VerifyDevicePasscodeUseCase {

    //region VerifyDevicePasscodeUseCase
    override suspend fun execute(devicePasscodePromptOptions: DevicePasscodePromptOptions): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState =
                stateRepository.get() ?: throw BusinessException.invalidState()
            val devicePasscodeUserVerificationHandler =
                operationState.devicePasscodeUserVerificationHandler
                    ?: throw BusinessException.invalidState()
            operationState.cancellableContinuation = cancellableContinuation
            operationState.devicePasscodeUserVerificationHandler = null
            devicePasscodeUserVerificationHandler.listenForOsCredentials(devicePasscodePromptOptions)
        }
    }
    //endregion
}
