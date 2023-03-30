/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for setting a new PIN during a registration operation.
 *
 * **IMPORTANT**: Prior to use this use-case one of the following use-cases must be executed:
 * - [AuthCloudApiRegistrationUseCase]
 * - [InBandRegistrationUseCase]
 * - [ProcessOutOfBandPayloadUseCase] with an [ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload]
 * object that contains registration related data
 *
 * This use-case must be executed after a [ch.nevis.exampleapp.coroutines.domain.model.response.EnrollPinResponse]
 * is received during a registration operation.
 */
interface SetPinUseCase {

    /**
     * Executes the use-case.
     *
     * @param pin The new PIN to be set during the registration.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(pin: CharArray): Response
}