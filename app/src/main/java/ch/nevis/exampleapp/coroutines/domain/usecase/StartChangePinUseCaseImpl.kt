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
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.mobile.sdk.api.operation.pin.PinChangeError
import ch.nevis.mobile.sdk.api.operation.pin.PinChanger
import ch.nevis.mobile.sdk.api.util.Consumer
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [StartChangePinUseCase] interface.
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of [ClientProvider] interface implementation.
 * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
 *  a [ChangePinOperationState].
 * @param pinChanger An instance of [PinChanger] interface implementation.
 * @param onSuccess An instance of a [Runnable] implementation.
 * @param onError An instance of a [Consumer] implementation that accepts a [PinChangeError] object.
 */
class StartChangePinUseCaseImpl(
    private val clientProvider: ClientProvider,
    private val stateRepository: OperationStateRepository<ChangePinOperationState>,
    private val pinChanger: PinChanger,
    private val onSuccess: Runnable,
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
