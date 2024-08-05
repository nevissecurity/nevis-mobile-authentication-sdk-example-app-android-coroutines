/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePasswordOperationState
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePinOperationState
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository

/**
 * Default implementation of [FinishOperationUseCase] interface that clears all [OperationStateRepository]
 * instances that used by the application.
 *
 * @constructor Creates a new instance.
 * @param userInteractionOperationStateRepository An instance of an [OperationStateRepository]
 *  implementation that may hold an [UserInteractionOperationState].
 * @param changePinOperationStateRepository An instance of an [OperationStateRepository] implementation
 *  that may hold an [ChangePinOperationState].
 * @param changePasswordOperationStateRepository An instance of an [OperationStateRepository]
 *  implementation that may hold an [ChangePasswordOperationState].
 */
class FinishOperationUseCaseImpl(
    private val userInteractionOperationStateRepository: OperationStateRepository<UserInteractionOperationState>,
    private val changePinOperationStateRepository: OperationStateRepository<ChangePinOperationState>,
    private val changePasswordOperationStateRepository: OperationStateRepository<ChangePasswordOperationState>
) : FinishOperationUseCase {

    //region FinishOperationUseCase
    override suspend fun execute() {
        userInteractionOperationStateRepository.get()?.reset()
        userInteractionOperationStateRepository.reset()

        changePinOperationStateRepository.get()?.reset()
        changePinOperationStateRepository.reset()

        changePasswordOperationStateRepository.get()?.reset()
        changePasswordOperationStateRepository.reset()
    }
    //endregion
}
