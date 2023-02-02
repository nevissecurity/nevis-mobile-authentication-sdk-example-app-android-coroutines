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
import ch.nevis.exampleapp.coroutines.domain.model.response.ErrorResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.PayloadDecodeCompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Default implementation of [DecodePayloadUseCase] interface.
 */
class DecodePayloadUseCaseImpl(
    /**
     * An instance of a [ClientProvider] implementation.
     */
    private val clientProvider: ClientProvider
) : DecodePayloadUseCase {

    //region DecodePayloadUseCase
    override suspend fun execute(json: String?, base64UrlEncoded: String?): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val client = clientProvider.get() ?: throw BusinessException.clientNotInitialized()
            val operation = client.operations().outOfBandPayloadDecode()
                .onSuccess {
                    cancellableContinuation.resume(
                        PayloadDecodeCompletedResponse(it)
                    )
                }
                .onError {
                    cancellableContinuation.resume(
                        ErrorResponse(
                            MobileAuthenticationClientException(
                                Operation.DECODE_OUT_OF_BAND_PAYLOAD,
                                it
                            )
                        )
                    )
                }
            json?.let {
                operation.json(it)
            }
            base64UrlEncoded?.let {
                operation.base64UrlEncoded(it)
            }
            operation.execute()
        }
    }
    //endregion
}