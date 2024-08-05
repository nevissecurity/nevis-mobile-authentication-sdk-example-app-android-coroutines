/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.localdata.Authenticator

/**
 * A [Response] class that holds the requested [Authenticator] set.
 *
 * @constructor Creates a new instance.
 * @param authenticators The requested [Authenticator] set.
 */
data class GetAuthenticatorsResponse(

    /**
     * The requested [Authenticator] set.
     */
    val authenticators: Set<Authenticator>
) : Response
