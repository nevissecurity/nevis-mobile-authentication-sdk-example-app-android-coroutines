/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAccount

import ch.nevis.mobile.sdk.api.localdata.Account


/**
 * Interface declaration of a listener that is used by [AccountsRecyclerViewAdapter] to notify the implementations
 * of this interface about account selection.
 */
interface AccountSelectedListener {

    /**
     * Event method that is called when an account is selected.
     *
     * @param account The selected account.
     */
    fun onAccountSelected(account: Account)
}