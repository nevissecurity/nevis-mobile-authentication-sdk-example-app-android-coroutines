/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.pin.PinEnrollmentError

/**
 * A [Response] class that indicates the registration operation is waiting for PIN enrollment.
 * To continue the operation after this response is received [ch.nevis.exampleapp.domain.usecase.SetPinUseCase]
 * has to be started with the new PIN entered by the user.
 */
class EnrollPinResponse(
    /**
     * The last recoverable error. It exists only if there was already a failed PIN enrollment attempt.
     */
    val lastRecoverableError: PinEnrollmentError?
) : Response
