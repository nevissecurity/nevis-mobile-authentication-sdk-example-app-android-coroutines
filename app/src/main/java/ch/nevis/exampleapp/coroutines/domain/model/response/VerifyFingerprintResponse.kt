/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerificationError

/**
 * Response class that indicates the SDK operation asks the user to verify herself/himself using fingerprint authentication.
 * Typically when this response is received a [ch.nevis.exampleapp.domain.usecase.VerifyFingerprintUseCase] is started.
 */
class VerifyFingerprintResponse(
    /**
     * The last recoverable error. It exists only if there was already a failed fingerprint verification attempt.
     */
    val lastRecoverableError: FingerprintUserVerificationError?
) : Response