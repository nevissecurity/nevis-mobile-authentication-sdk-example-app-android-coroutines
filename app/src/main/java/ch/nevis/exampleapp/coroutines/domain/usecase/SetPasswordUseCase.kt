/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for setting a new password during a registration operation.
 *
 * **IMPORTANT**: Prior to use this use-case one of the following use-cases must be executed:
 * - [AuthCloudApiRegistrationUseCase]
 * - [InBandRegistrationUseCase]
 * - [ProcessOutOfBandPayloadUseCase] with an [ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload]
 * object that contains registration related data
 *
 * This use-case must be executed after a [ch.nevis.exampleapp.coroutines.domain.model.response.EnrollPasswordResponse]
 * is received during a registration operation.
 */
interface SetPasswordUseCase {

    /**
     * Executes the use-case.
     *
     * @param password The new password to be set during the registration.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(password: CharArray): Response
}
