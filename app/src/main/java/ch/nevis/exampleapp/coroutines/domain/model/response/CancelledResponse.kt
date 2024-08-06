/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation

/**
 * Response class indicates that the user cancelled the operation, the operation stopped.
 *
 * @constructor Creates a new instance.
 * @param operation The [Operation] that was cancelled if it can be determined otherwise null.
 */
class CancelledResponse(
    /**
     * The [Operation] that was cancelled if it can be determined otherwise null.
     */
    val operation: Operation? = null
) : Response
