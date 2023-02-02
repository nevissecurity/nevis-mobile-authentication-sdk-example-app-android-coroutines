/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

/**
 * Response class that indicates the operation stopped with an error.
 */
class ErrorResponse(
    /**
     * The [Throwable] object the represents the error.
     */
    val cause: Throwable
) : Response