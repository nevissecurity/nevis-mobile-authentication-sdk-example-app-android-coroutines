/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for starting an Auth Cloud API registration operation.
 */
interface AuthCloudApiRegistrationUseCase {

    /**
     * Executes the use-case.
     *
     * @param enrollResponse The response of the Cloud HTTP API to the enroll endpoint.
     * @param appLinkUri The value of the `appLinkUri` attribute in the enroll response sent by the server.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(enrollResponse: String?, appLinkUri: String?): Response
}
