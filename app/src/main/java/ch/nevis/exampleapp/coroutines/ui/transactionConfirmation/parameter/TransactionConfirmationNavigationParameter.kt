/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022-2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.transactionConfirmation.parameter

import android.os.Parcelable
import ch.nevis.mobile.sdk.api.localdata.Account
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter class for Transaction Confirmation view.
 *
 * @constructor Creates a new instance.
 * @param account The previously selected account.
 * @param transactionConfirmationMessage The transaction confirmation data/message that should be displayed
 * on Transaction Confirmation view.
 */
@Parcelize
data class TransactionConfirmationNavigationParameter(

    /**
     * The previously selected account.
     */
    @IgnoredOnParcel
    val account: Account? = null,

    /**
     * The transaction confirmation data as a [String].
     */
    val transactionConfirmationMessage: String
) : Parcelable
