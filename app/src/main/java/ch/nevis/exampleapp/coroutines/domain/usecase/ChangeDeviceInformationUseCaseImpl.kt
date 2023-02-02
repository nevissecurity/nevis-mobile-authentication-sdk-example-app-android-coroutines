/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.error.MobileAuthenticationClientException
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.response.CompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Default implementation of [ChangeDeviceInformationUseCase] interface.
 */
class ChangeDeviceInformationUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider
) : ChangeDeviceInformationUseCase {

    //region ChangeDeviceInformationUseCase
    override suspend fun execute(
        name: String,
        fcmRegistrationToken: String?,
        disablePushNotifications: Boolean
    ): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            val operation = client.operations().deviceInformationChange()
                .name(name)
                .onSuccess {
                    cancellableContinuation.resume(
                        CompletedResponse(Operation.CHANGE_DEVICE_INFORMATION)
                    )
                }
                .onError {
                    cancellableContinuation.resume(
                        ErrorResponse(
                            MobileAuthenticationClientException(
                                Operation.CHANGE_DEVICE_INFORMATION,
                                it
                            )
                        )
                    )
                }

            fcmRegistrationToken?.let {
                operation.fcmRegistrationToken(it)
            }

            if (disablePushNotifications) {
                operation.disablePushNotifications()
            }

            operation.execute()
        }
    }
    //endregion
}