/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePasswordOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.mobile.sdk.api.operation.password.PasswordChangeError
import ch.nevis.mobile.sdk.api.operation.password.PasswordChanger
import ch.nevis.mobile.sdk.api.util.Consumer
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [StartChangePasswordUseCase] interface.
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of [ClientProvider] interface implementation.
 * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
 *  a [ChangePasswordOperationState].
 * @param passwordChanger An instance of [PasswordChanger] interface implementation.
 * @param onSuccess An instance of a [Runnable] implementation.
 * @param onError An instance of a [Consumer] implementation that accepts a [PasswordChangeError] object.
 */
class StartChangePasswordUseCaseImpl(
    private val clientProvider: ClientProvider,
    private val stateRepository: OperationStateRepository<ChangePasswordOperationState>,
    private val passwordChanger: PasswordChanger,
    private val onSuccess: Runnable,
    private val onError: Consumer<PasswordChangeError>
) : StartChangePasswordUseCase {

    //region StartChangePasswordUseCase
    override suspend fun execute(username: String): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val operationState = ChangePasswordOperationState()
            operationState.cancellableContinuation = cancellableContinuation
            stateRepository.save(operationState)

            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            client.operations().passwordChange()
                .username(username)
                .passwordChanger(passwordChanger)
                .onSuccess(onSuccess)
                .onError(onError)
                .execute()
        }
    }
    //endregion
}
