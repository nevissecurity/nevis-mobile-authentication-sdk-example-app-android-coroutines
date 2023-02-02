/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload

/**
 * Use-case interface for starting a process out-of-band payload operation.
 *
 * **NOTE**: An [OutOfBandPayload] object can be obtained by running the [DecodePayloadUseCase] with
 * proper payload data.
 */
interface ProcessOutOfBandPayloadUseCase {

    /**
     * Executes the use-case.
     *
     * @param payload The [OutOfBandPayload] object to be processed.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(payload: OutOfBandPayload): Response
}