/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider

/**
 * Use-case interface for starting an in-band registration operation.
 */
interface InBandRegistrationUseCase {

    /**
     * Executes the use-case.
     *
     * @param extId The external identifier of the user to be registered.
     * @param authorizationProvider The [AuthorizationProvider] that must be used to register.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(extId: String, authorizationProvider: AuthorizationProvider?): Response
}
