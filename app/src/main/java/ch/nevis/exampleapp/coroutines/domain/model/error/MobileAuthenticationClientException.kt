/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.error

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.mobile.sdk.api.MobileAuthenticationClientError

/**
 * A sub-class of [Exception] that hold a [MobileAuthenticationClientError] and an optional [Operation] value.
 */
class MobileAuthenticationClientException(

    /**
     * The [Operation] the error relates to or null if it cannot be determined.
     */
    val operation: Operation? = null,

    /**
     * The [MobileAuthenticationClientError] object that represents the error.
     */
    val error: MobileAuthenticationClientError
) : Exception()