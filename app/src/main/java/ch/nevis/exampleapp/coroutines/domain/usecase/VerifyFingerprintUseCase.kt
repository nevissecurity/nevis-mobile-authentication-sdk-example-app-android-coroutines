/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for verifying the user using fingerprint authenticator.
 *
 * **IMPORTANT**: Prior to use this use-case one of the following use-cases must be executed:
 * - [AuthCloudApiRegistrationUseCase]
 * - [InBandAuthenticationUseCase]
 * - [InBandRegistrationUseCase]
 * - [ProcessOutOfBandPayloadUseCase]
 *
 * This use-case must be executed after a [ch.nevis.exampleapp.domain.model.response.VerifyFingerprintResponse]
 * is received during an authentication or registration operation.
 */
interface VerifyFingerprintUseCase {

    /**
     * Executes the use-case.
     *
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(): Response
}