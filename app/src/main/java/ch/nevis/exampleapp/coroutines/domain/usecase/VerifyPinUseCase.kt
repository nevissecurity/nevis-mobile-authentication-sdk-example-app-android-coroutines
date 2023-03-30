/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for verifying the user using biometric authenticator.
 *
 * **IMPORTANT**: Prior to use this use-case one of the following use-cases must be executed:
 * - [InBandAuthenticationUseCase]
 * - [ProcessOutOfBandPayloadUseCase] with an [ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload]
 * object that contains authentication related data
 *
 * This use-case must be executed after a [ch.nevis.exampleapp.coroutines.domain.model.response.VerifyPinResponse]
 * is received during an authentication operation.
 */
interface VerifyPinUseCase {

    /**
     * Executes the use-case.
     *
     * @param pin The PIN entered by the user for verification.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(pin: CharArray): Response
}