/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.qrReader

import com.google.mlkit.vision.barcode.common.Barcode

/**
 * Listener interface that is used by the [QrCodeAnalyzer] to notify listeners when a barcode(s)
 * successfully are read.
 */
interface BarcodesReceivedListener {

    /**
     * Notifies listeners about event barcode(s) are read, received.
     *
     * @param barcodes The read/received barcode(s).
     */
    fun onBarcodesReceived(barcodes: List<Barcode>)
}
