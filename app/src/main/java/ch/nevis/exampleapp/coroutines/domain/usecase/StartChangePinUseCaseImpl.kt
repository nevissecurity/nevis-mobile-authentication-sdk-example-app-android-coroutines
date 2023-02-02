/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePinOperationState
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.mobile.sdk.api.operation.pin.PinChangeError
import ch.nevis.mobile.sdk.api.operation.pin.PinChanger
import ch.nevis.mobile.sdk.api.util.Consumer
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [StartChangePinUseCase] interface.
 */
class StartChangePinUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider,

    /**
     * An instance of a [OperationStateRepository] implementation that may hold an [UserInteractionOperationState].
     */
    private val stateRepository: OperationStateRepository<ChangePinOperationState>,

    /**
     * An instance of a [PinChanger] implementation.
     */
    private val pinChanger: PinChanger,

    /**
     * An instance of a [Runnable] implementation.
     */
    private val onSuccess: Runnable,

    /**
     * An instance of a [Consumer] implementation that accepts a [PinChangeError] object.
     */
    private val onError: Consumer<PinChangeError>
) : StartChangePinUseCase {

    //region StartChangePinUseCase
    override suspend fun execute(username: String): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState = ChangePinOperationState()
            operationState.cancellableContinuation = cancellableContinuation
            stateRepository.save(operationState)

            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            client.operations().pinChange()
                .username(username)
                .pinChanger(pinChanger)
                .onSuccess(onSuccess)
                .onError(onError)
                .execute()
        }
    }
    //endregion
}