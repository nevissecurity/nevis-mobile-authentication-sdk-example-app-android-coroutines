/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.parameter

import android.os.Parcelable
import ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model.AuthenticatorItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter data class for select authenticator view.
 *
 * @constructor Creates a new instance.
 * @param authenticatorItems The list of available authenticator items the user can select from.
 */
@Parcelize
data class SelectAuthenticatorNavigationParameter(
    /**
     * The list of available authenticator the user can select from.
     */
    @IgnoredOnParcel
    val authenticatorItems: Set<AuthenticatorItem>? = null
) : Parcelable
