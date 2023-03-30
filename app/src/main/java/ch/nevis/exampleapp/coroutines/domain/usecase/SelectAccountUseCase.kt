/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.usecase

import ch.nevis.exampleapp.coroutines.domain.model.response.Response

/**
 * Use-case interface for selecting an account during an out-of-band authentication.
 *
 * **IMPORTANT**: Prior to use this use-case [ProcessOutOfBandPayloadUseCase] must be executed with an
 * [ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandPayload] object that contains authentication
 * related data that will start an out-of-band authentication. This use-case must be executed after a
 * [ch.nevis.exampleapp.coroutines.domain.model.response.SelectAccountResponse] is received during the out-of-band
 * authentication operation.
 */
interface SelectAccountUseCase {

    /**
     * Executes the use-case.
     *
     * @param username The selected username/account to continue the out-of-band operation with.
     * @return A [Response] object that indicates the result of the use-case execution.
     */
    suspend fun execute(username: String): Response
}