/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for selecting an authenticator during an authentication or registration operation.
 *
 * **IMPORTANT**: Prior to use this use-case one of the following use-cases must be executed:
 * - [AuthCloudApiRegistrationUseCase]
 * - [InBandAuthenticationUseCase]
 * - [InBandRegistrationUseCase]
 * - [ProcessOutOfBandPayloadUseCase]
 *
 * This use-case must be executed after a [ch.nevis.exampleapp.coroutines.domain.model.response.SelectAuthenticatorResponse]
 * is received during an authentication or registration operation.
 */
interface SelectAuthenticatorUseCase {

    /**
     * Executes the use-case.
     *
     * @param aaid The AAID of the selected authenticator to continue the authentication or registration
     * operation with.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(aaid: String): Response
}
