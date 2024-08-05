/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.interaction

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.AuthenticationCompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider
import ch.nevis.mobile.sdk.api.util.Consumer
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * An authentication specific implementation of [Consumer] interface that accepts an [AuthorizationProvider]
 * instance after the successful authentication. It resumes the cancellableContinuation found in operation
 * state with [AuthenticationCompletedResponse] indicating that the running authentication operation
 * successfully completed.
 *
 * @constructor Creates a new instance.
 * @param stateRepository The state repository that stores the state of the running operation.
 * @param forOperation An optional [Operation] value the authentication started for. In some cases an
 *  operation needs a pre-step authentication. In these cases an instance of this class can be initialized
 *  with the proper operation and this value will be set to the [AuthenticationCompletedResponse] object
 *  the cancellableContinuation of the operation state resumed with.
 */
class OnSuccessAuthenticationImpl(
    private val stateRepository: OperationStateRepository<UserInteractionOperationState>,
    private val forOperation: Operation? = null
) : Consumer<AuthorizationProvider> {

    //region Consumer
    /** @suppress */
    override fun accept(authorizationProvider: AuthorizationProvider) {
        val operationState =
            stateRepository.get() ?: throw BusinessException.invalidState()
        val cancellableContinuation =
            operationState.cancellableContinuation ?: throw BusinessException.invalidState()
        val username = operationState.username ?: throw BusinessException.invalidState()

        Timber.asTree().sdk(
            String.format(
                "Operation %s successfully completed.",
                operationState.operation.name
            )
        )

        operationState.reset()
        stateRepository.reset()

        cancellableContinuation.resume(
            AuthenticationCompletedResponse(
                authorizationProvider,
                username,
                forOperation
            )
        )
    }
    //endregion
}
