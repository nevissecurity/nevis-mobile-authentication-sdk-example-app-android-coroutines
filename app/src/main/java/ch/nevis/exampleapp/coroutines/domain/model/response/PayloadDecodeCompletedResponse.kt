/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload

/**
 * A [Response] class that indicates decoding of out-of-band payload was successfully completed.
 * Typically when this response is received a [ch.nevis.exampleapp.coroutines.domain.usecase.ProcessOutOfBandPayloadUseCase]
 * is started with the received payload object.
 */
class PayloadDecodeCompletedResponse(

    /**
     * The decoded [OutOfBandPayload] object.
     */
    val payload: OutOfBandPayload
) : Response