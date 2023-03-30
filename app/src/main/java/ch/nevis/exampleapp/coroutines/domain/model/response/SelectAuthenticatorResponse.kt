/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.localdata.Authenticator

/**
 * [Response] class that indicates an authenticator selection has to be started.
 * Typically the received [Authenticator] set is shown to the user and he/she selects one of them.
 * After the authenticator selection [ch.nevis.exampleapp.coroutines.domain.usecase.SelectAuthenticatorUseCase] is called
 * to continue the operation.
 */
class SelectAuthenticatorResponse(
    /**
     * The set of available authenticators the user has to choose from.
     */
    val authenticators: Set<Authenticator>
) : Response
