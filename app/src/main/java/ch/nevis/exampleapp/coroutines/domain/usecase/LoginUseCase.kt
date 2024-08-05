/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import java.net.PasswordAuthentication

/**
 * Interface declaration of legacy login use-case.
 */
interface LoginUseCase {

    /**
     * Executes the use-case. Starts legacy login with the given username and password to obtain
     * HTTP session information (cookies) for an in-band registration.
     *
     * @param passwordAuthentication The [PasswordAuthentication] object that holds the username and
     * the password be used to log in.
     * @return The response of the use-case execution.
     */
    suspend fun execute(passwordAuthentication: PasswordAuthentication): Response
}
