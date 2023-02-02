/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.transactionConfirmation.parameter

import android.os.Parcelable
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.mobile.sdk.api.localdata.Account
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Navigation parameter class for Transaction Confirmation view.
 */
@Parcelize
data class TransactionConfirmationNavigationParameter(

    /**
     * The operation type the account was selection requested for.
     */
    val operation: Operation,

    /**
     * The list of available accounts the user can select from.
     */
    val accounts: @RawValue Set<Account>,

    /**
     * The transaction confirmation data as a [String].
     */
    val transactionConfirmationData: String
) : Parcelable