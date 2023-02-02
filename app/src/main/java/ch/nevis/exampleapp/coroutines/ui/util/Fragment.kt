/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.util

import androidx.fragment.app.Fragment

/**
 * Fragment argument key constant for dispatch token response.
 */
val FRAGMENT_ARGUMENT_DISPATCH_TOKEN_RESPONSE: String
    get() = "dispatchTokenResponse"

/**
 * Extension function for [Fragment] class that simplifies handling of dispatch token response argument of a fragment.
 *
 * @param block The lambda block that is called with the received dispatch token response if it exists.
 */
fun Fragment.handleDispatchTokenResponse(block: (dispatchTokenResponse: String) -> Unit) {
    arguments?.getString(FRAGMENT_ARGUMENT_DISPATCH_TOKEN_RESPONSE)?.let {
        block(it)
    }
    arguments?.remove(FRAGMENT_ARGUMENT_DISPATCH_TOKEN_RESPONSE)
}
