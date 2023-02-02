/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.util

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.ui.result.ResultFragmentDirections

/**
 * Extension function for [NavController] to ease navigation to home screen.
 */
fun NavController.navigateToHome() {
    navigate(ResultFragmentDirections.actionGlobalHomeFragment())
}

/**
 * Extension function for [NavController] to ease navigation to home screen with a dispatch token response.
 *
 * @param dispatchTokenResponse The dispatch token response that will be processed by the home screen.
 */
fun NavController.navigateToHome(dispatchTokenResponse: String) {
    val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
    navigate(
        R.id.homeFragment,
        bundleOf(FRAGMENT_ARGUMENT_DISPATCH_TOKEN_RESPONSE to dispatchTokenResponse),
        navOptions
    )
}