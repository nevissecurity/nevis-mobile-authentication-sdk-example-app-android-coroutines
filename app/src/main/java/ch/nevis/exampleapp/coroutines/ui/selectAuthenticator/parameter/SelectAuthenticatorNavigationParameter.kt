/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.parameter

import android.os.Parcelable
import ch.nevis.mobile.sdk.api.localdata.Authenticator
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Navigation parameter data class for select authenticator view.
 */
@Parcelize
data class SelectAuthenticatorNavigationParameter(
    /**
     * The list of available authenticator the user can select from.
     */
    val authenticators: @RawValue Set<Authenticator>
) : Parcelable