/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.domain.model.response

import androidx.annotation.StringRes

/**
 * Response class that indicates the SDK operation asks the user to verify herself/himself using device
 * passcode authentication. Typically when this response is received a
 * [ch.nevis.exampleapp.coroutines.domain.usecase.VerifyDevicePasscodeUseCase] is started.
 */
class VerifyDevicePasscodeResponse(
    /**
     * String resource identifier of the title of the authenticator.
     */
    @StringRes
    val authenticatorTitleResId: Int
) : Response