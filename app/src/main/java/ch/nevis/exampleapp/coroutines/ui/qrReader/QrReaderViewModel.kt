/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.qrReader

import ch.nevis.exampleapp.coroutines.ui.base.OutOfBandOperationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * View model implementation of QR Reader view.
 *
 * @constructor Creates a new instance.
 */
@HiltViewModel
class QrReaderViewModel @Inject constructor() : OutOfBandOperationViewModel()
