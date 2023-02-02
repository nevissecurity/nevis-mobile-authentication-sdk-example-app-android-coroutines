/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation

/**
 * Response class that indicates the previously started operation successfully completed.
 */
class CompletedResponse(
    /**
     * The [Operation] that was successfully completed.
     */
    val operation: Operation
) : Response