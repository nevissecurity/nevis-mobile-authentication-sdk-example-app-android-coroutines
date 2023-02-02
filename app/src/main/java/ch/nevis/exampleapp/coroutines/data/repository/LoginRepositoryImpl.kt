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
 */
class LoginRepositoryImpl(
    /**
     * A [LoginDataSource] instance to extract the [LoginResponse] from data layer.
     */
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