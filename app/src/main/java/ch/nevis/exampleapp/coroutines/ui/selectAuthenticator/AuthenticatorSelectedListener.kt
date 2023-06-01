/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator


/**
 * Interface declaration of a listener that is used by [AuthenticatorsRecyclerViewAdapter] to notify the implementations
 * of this interface about authentication selection.
 */
interface AuthenticatorSelectedListener {

    /**
     * Event method that is called when an authenticator is selected.
     *
     * @param aaid The AAID of selected authenticator.
     */
    fun onAuthenticatorSelected(aaid: String)
}