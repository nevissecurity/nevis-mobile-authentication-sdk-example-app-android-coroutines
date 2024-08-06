/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider

/**
 * A [Response] class that indicates the authentication operation was successfully completed.
 *
 * @constructor Creates a new instance.
 * @param authorizationProvider The [AuthorizationProvider] object received after the successful authentication.
 * @param username The username that identifies the account object that was used during the authentication.
 * @param forOperation The [Operation] that needed this authentication and the [AuthorizationProvider] object.
 */
data class AuthenticationCompletedResponse(
    /**
     * The [AuthorizationProvider] object received after the successful authentication.
     */
    val authorizationProvider: AuthorizationProvider,

    /**
     * The username that identifies the account object that was used during the authentication.
     */
    val username: String,

    /**
     * The [Operation] that needed this authentication and the [AuthorizationProvider] object.
     */
    val forOperation: Operation? = null
) : Response
