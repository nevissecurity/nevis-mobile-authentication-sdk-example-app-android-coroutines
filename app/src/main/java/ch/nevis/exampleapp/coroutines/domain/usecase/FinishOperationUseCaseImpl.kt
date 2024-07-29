/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePinOperationState
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository

/**
 * Default implementation of [FinishOperationUseCase] interface that clears all [OperationStateRepository]
 * instances that used by the application.
 */
class FinishOperationUseCaseImpl(
    /**
     * An instance of an [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val userInteractionOperationStateRepository: OperationStateRepository<UserInteractionOperationState>,

    /**
     * An instance of an [OperationStateRepository] implementation that may hold an [ChangePinOperationState].
     */
    private val changePinOperationStateRepository: OperationStateRepository<ChangePinOperationState>
) : FinishOperationUseCase {

    //region FinishOperationUseCase
    override suspend fun execute() {
        userInteractionOperationStateRepository.get()?.let {
            it.reset()
        }
        userInteractionOperationStateRepository.reset()

        changePinOperationStateRepository.get()?.let {
            it.reset()
        }
        changePinOperationStateRepository.reset()
    }
    //endregion
}
