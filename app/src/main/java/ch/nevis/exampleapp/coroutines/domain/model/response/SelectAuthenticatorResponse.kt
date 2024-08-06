/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model.AuthenticatorItem
import ch.nevis.mobile.sdk.api.localdata.Authenticator

/**
 * [Response] class that indicates an authenticator selection has to be started.
 * Typically the received [Authenticator] set is shown to the user and he/she selects one of them.
 * After the authenticator selection [ch.nevis.exampleapp.coroutines.domain.usecase.SelectAuthenticatorUseCase] is called
 * to continue the operation.
 *
 * @constructor Creates a new instance.
 * @param authenticatorItems The set of available authenticators the user has to choose from.
 */
class SelectAuthenticatorResponse(
    /**
     * The set of available authenticators the user has to choose from.
     */
    val authenticatorItems: Set<AuthenticatorItem>
) : Response
