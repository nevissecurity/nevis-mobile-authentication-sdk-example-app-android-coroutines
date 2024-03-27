/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import androidx.annotation.StringRes

/**
 * Response class that indicates the SDK operation asks the user to verify herself/himself using biometric
 * (face/fingerprint) authentication.
 * Typically when this response is received a [ch.nevis.exampleapp.coroutines.domain.usecase.VerifyBiometricUseCase] is started.
 */
class VerifyBiometricResponse(
    /**
     * String resource identifier of the title of the authenticator.
     */
    @StringRes
    val authenticatorTitleResId: Int
) : Response