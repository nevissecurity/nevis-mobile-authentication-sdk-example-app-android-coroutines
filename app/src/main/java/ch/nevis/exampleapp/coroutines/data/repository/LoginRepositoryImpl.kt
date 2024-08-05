/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.data.repository

import ch.nevis.exampleapp.coroutines.data.dataSource.LoginDataSource
import ch.nevis.exampleapp.coroutines.domain.model.response.LoginResponse
import ch.nevis.exampleapp.coroutines.domain.repository.LoginRepository
import java.net.PasswordAuthentication

/**
 * Default implementation of [LoginRepository] interface.
 *
 * @constructor Creates a new instance.
 * @param loginDataSource A [LoginDataSource] instance to extract the [LoginResponse] from data layer.
 */
class LoginRepositoryImpl(
    private val loginDataSource: LoginDataSource
) : LoginRepository {

    //region LoginRepository
    override suspend fun execute(passwordAuthentication: PasswordAuthentication): LoginResponse {
        val loginDataSourceResponse = loginDataSource.execute(passwordAuthentication)
        return LoginResponse(
            loginDataSourceResponse.extId,
            loginDataSourceResponse.cookies
        )
    }
    //endregion
}
