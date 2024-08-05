/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for decoding an out-of-band payload.
 *
 */
interface DecodePayloadUseCase {

    /**
     * Executes the use-case.
     *
     * **IMPORTANT**: You must provide either the JSON through this method or the Base64 URL encoded
     * representation of the JSON.
     *
     * @param json: Specifies the JSON to be decoded.
     * @param base64UrlEncoded: Specifies the JSON as Base64 URL encoded [String] to be decoded.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(json: String?, base64UrlEncoded: String?): Response
}
