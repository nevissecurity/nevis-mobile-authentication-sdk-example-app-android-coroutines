/*
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2026. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.exampleapp.coroutines.domain.usecase.ProcessOutOfBandPayloadUseCase
import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload

/**
 * A [Response] interface for response classes that carry an [OutOfBandPayload] object that is
 * ready to be processed by [ProcessOutOfBandPayloadUseCase].
 */
interface OutOfBandPayloadResponse : Response {

    /**
     * The [OutOfBandPayload] object to be processed.
     */
    val payload: OutOfBandPayload
}
