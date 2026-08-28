/*
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2026. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.exampleapp.coroutines.domain.usecase.ProcessOutOfBandPayloadUseCase
import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload

/**
 * A [Response] class that indicates a pending out-of-band operation was found.
 * Typically, when this response is received a [ProcessOutOfBandPayloadUseCase]
 * is started with the received payload object.
 *
 * @constructor Creates a new instance.
 * @param payload The [OutOfBandPayload] object of the pending out-of-band operation.
 */
class PendingOperationsFoundResponse(

    /**
     * The [OutOfBandPayload] object of the pending out-of-band operation.
     */
    val payload: OutOfBandPayload
) : Response
