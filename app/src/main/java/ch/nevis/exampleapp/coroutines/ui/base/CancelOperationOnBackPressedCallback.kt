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
 */
class CancelOperationOnBackPressedCallback(

    /**
     * The view model that runs/handles the cancellable operation.
     */
    private val viewModel: CancellableOperationViewModel
) : OnBackPressedCallback(true) {

    //region OnBackPressedCallback
    override fun handleOnBackPressed() {
        viewModel.cancelOperation()
    }
    //endregion
}