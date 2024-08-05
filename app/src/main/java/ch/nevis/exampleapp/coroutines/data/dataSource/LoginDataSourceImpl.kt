/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.data.dataSource

import ch.nevis.exampleapp.coroutines.data.retrofit.LoginEndPoint
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.response.LoginResponse
import ch.nevis.mobile.sdk.api.authorization.Cookie
import retrofit2.Retrofit
import java.net.PasswordAuthentication

/**
 * Default implementation of [LoginDataSource] interface that uses Retrofit to retrieve the credentials
 * from the server for the given username/password.
 *
 * @constructor Creates a new instance.
 * @param retrofit An instance of [Retrofit].
 *
 */
class LoginDataSourceImpl(
    private val retrofit: Retrofit
) : LoginDataSource {

    //region LoginDataSource
    override suspend fun execute(passwordAuthentication: PasswordAuthentication): LoginResponse {
        val baseUri = retrofit.baseUrl().toUri()
        val loginEndPoint = retrofit.create(LoginEndPoint::class.java)

        val retrofitResponse = loginEndPoint.authenticate(
            passwordAuthentication.userName,
            String(passwordAuthentication.password)
        )
        val loginResponse = retrofitResponse.body() ?: throw BusinessException.loginFailed()
        val cookies = mutableListOf<Cookie>()
        for (cookieString in retrofitResponse.headers()
            .values(HEADER_SET_COOKIE)) {
            cookies.add(Cookie.create(baseUri, cookieString))
        }
        return LoginResponse(
            loginResponse.extId,
            cookies
        )
    }
    //endregion

    //region Companion Object
    /**
     * Collection of constants.
     */
    companion object {
        /**
         * The `Set-Cookie` header key.
         */
        private const val HEADER_SET_COOKIE = "Set-Cookie"
    }
    //endregion
}
