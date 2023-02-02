/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.data.model

/**
 * Data class that represents the JSON response of [ch.nevis.exampleapp.data.retrofit.LoginEndPoint].
 */
data class LoginEndPointResponse(
    /**
     * Status, result of the login end-point call.
     */
    val status: String,

    /**
     * The external identifier of the user that logged in.
     */
    val extId: String
)
