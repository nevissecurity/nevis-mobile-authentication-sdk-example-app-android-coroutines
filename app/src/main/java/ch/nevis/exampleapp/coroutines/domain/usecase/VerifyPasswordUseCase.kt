/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for verifying the user using Password authenticator.
 *
 * **IMPORTANT**: Prior to use this use-case one of the following use-cases must be executed:
 * - [InBandAuthenticationUseCase]
 * - [ProcessOutOfBandPayloadUseCase] with an [ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload]
 * object that contains authentication related data
 *
 * This use-case must be executed after a [ch.nevis.exampleapp.coroutines.domain.model.response.VerifyPasswordResponse]
 * is received during an authentication operation.
 */
interface VerifyPasswordUseCase {

    /**
     * Executes the use-case.
     *
     * @param password The password entered by the user for verification.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(password: CharArray): Response
}
