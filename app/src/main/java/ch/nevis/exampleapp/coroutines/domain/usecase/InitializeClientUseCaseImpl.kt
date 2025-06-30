/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import android.content.Context
import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.MobileAuthenticationClientException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.InitializeClientCompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.mobile.sdk.api.Configuration
import ch.nevis.mobile.sdk.api.MobileAuthenticationClientInitializer
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Semaphore
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * Default implementation of [InitializeClientUseCase] interface.
 *
 * To make client and application initialization properly work only one client initialization must
 * be run at a time. It is ensured by two things:
 * - Using a [Semaphore] object in this implementation.
 * - Only one instance must be exist of this implementation. It is ensured by injecting this implementation
 * as singleton by Dagger Hilt.
 *
 * @constructor Creates a new instance.
 * @param clientProvider An instance of [ClientProvider] interface implementation.
 * @param context An Android [Context] object used for initializing [ch.nevis.mobile.sdk.api.MobileAuthenticationClient].
 */
class InitializeClientUseCaseImpl(
    private val clientProvider: ClientProvider,
    private val context: Context,
) : InitializeClientUseCase {

    /**
     * A [Semaphore] object that is used to ensure only one client initialization is executed.
     */
    private val semaphore = Semaphore(1)

    //region InitializeClientUseCase
    override suspend fun execute(configuration: Configuration): Response {
        semaphore.acquire()
        val client = clientProvider.get()
        return if (client != null) {
            Timber.asTree().sdk("Client already initialized.")
            semaphore.release()
            InitializeClientCompletedResponse()
        } else {
            suspendCancellableCoroutine { cancellableContinuation ->
                MobileAuthenticationClientInitializer.initializer()
                    .context(context)
                    .configuration(configuration)
                    .onSuccess {
                        Timber.asTree().sdk("Client initialization succeeded.")
                        clientProvider.save(it)
                        cancellableContinuation.resume(InitializeClientCompletedResponse())
                        semaphore.release()
                    }
                    .onError {
                        cancellableContinuation.resume(
                            ErrorResponse(
                                MobileAuthenticationClientException(
                                    Operation.INIT_CLIENT,
                                    it
                                )
                            )
                        )
                        semaphore.release()
                    }
                    .execute()
            }
        }
    }
    //endregion
}
