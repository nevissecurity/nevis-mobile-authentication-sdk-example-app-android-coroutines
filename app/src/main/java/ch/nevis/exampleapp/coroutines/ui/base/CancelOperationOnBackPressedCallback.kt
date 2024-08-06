/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.base

import androidx.activity.OnBackPressedCallback

/**
 * A common implementation of [OnBackPressedCallback] class that cancels
 * the running operation if there is any.
 *
 * @constructor Creates a new instance.
 * @param viewModel The view model that runs/handles the cancellable operation.
 */
open class CancelOperationOnBackPressedCallback(
    private val viewModel: CancellableOperationViewModel
) : OnBackPressedCallback(true) {

    //region OnBackPressedCallback
    /** @suppress */
    override fun handleOnBackPressed() {
        viewModel.cancelOperation()
    }
    //endregion
}
