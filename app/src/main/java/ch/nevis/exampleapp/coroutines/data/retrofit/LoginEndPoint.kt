/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.data.retrofit

import ch.nevis.exampleapp.coroutines.data.model.LoginEndPointResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Retrofit end-point interface for login.
 */
interface LoginEndPoint {

    /**
     * Authenticates with the given username and password.
     *
     * @param username The username for authentication.
     * @param password The password for authentication.
     * @return On successful authentication a [Response] object that contains the HTTP body as a [LoginEndPointResponse] object.
     * @throws Exception On failed login an exception is thrown that may indicate network specific error or it could be
     * a [com.google.gson.stream.MalformedJsonException] when invalid credentials are sent to the server.
     */
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    @POST("/auth/pwd")
    suspend fun authenticate(
        @Field("isiwebuserid") username: String,
        @Field("isiwebpasswd") password: String
    ): Response<LoginEndPointResponse>
}
