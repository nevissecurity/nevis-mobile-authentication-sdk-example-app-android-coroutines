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
import ch.nevis.mobile.sdk.api.Configuration
import ch.nevis.mobile.sdk.api.MobileAuthenticationClientInitializer
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Semaphore
import kotlin.coroutines.resume

/**
 * Default implementation of [InitializeClientUseCase] interface.
 *
 * To make client and application initialization properly work only one client initialization must
 * be run at a time. It is ensured by two things:
 * - Using a [Semaphore] object in this implementation.
 * - Only one instance must be exist of this implementation. It is ensured by injecting this implementation
 * as singleton by Dagger Hilt.
 */
class InitializeClientUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider,

    /**
     * An Android [Context] object used for initializing [ch.nevis.mobile.sdk.api.MobileAuthenticationClient].
     */
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
            semaphore.release()
            InitializeClientCompletedResponse()
        } else {
            suspendCancellableCoroutine { cancellableContinuation ->
                MobileAuthenticationClientInitializer.initializer()
                    .context(context)
                    .configuration(configuration)
                    .onSuccess {
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
