/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.data.dataSource

import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.LoginResponse
import java.net.PasswordAuthentication

/**
 * Interface declaration of a login datasource that can be used to retrieve login [LoginResponse] for
 * a username/password pair. The received [LoginResponse] can be used for authentication
 * in further steps.
 */
interface LoginDataSource {

    /**
     * Executes the datasource, retrieves the [LoginResponse] object for the username/password pair.
     *
     * @param passwordAuthentication The [PasswordAuthentication] object that holds the username and
     * the password be used to log in.
     * @return The [LoginResponse] object for further authentication.
     * @throws BusinessException If the provided username/password pair is not valid.
     */
    suspend fun execute(passwordAuthentication: PasswordAuthentication): LoginResponse
}