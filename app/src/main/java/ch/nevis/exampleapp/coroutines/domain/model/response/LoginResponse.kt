/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.authorization.Cookie

/**
 * A [Response] class that represents the result of a successful legacy login.
 *
 * @constructor Creates a new instance.
 * @param extId External identifier of the user.
 * @param cookies List of [Cookie] for further authentication, e.g. for [ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider.CookieAuthorizationProvider]
 *  creation.
 */
data class LoginResponse(

    /**
     * External identifier of the user.
     */
    val extId: String,

    /**
     * List of [Cookie] for further authentication, e.g. for [ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider.CookieAuthorizationProvider] creation.
     */
    val cookies: List<Cookie>
) : Response
