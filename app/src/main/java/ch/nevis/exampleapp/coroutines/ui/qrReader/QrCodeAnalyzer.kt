/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.qrReader

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

/**
 * A [ImageAnalysis.Analyzer] implementation for QR Reader view to process images as barcodes.
 */
class QrCodeAnalyzer(
    /**
     * A [BarcodesReceivedListener] implementation that will be notified when barcode(s) are read/received.
     */
    private val barcodesReceivedListener: BarcodesReceivedListener
) : ImageAnalysis.Analyzer {

    //region Properties
    /**
     * Options for barcode scanner initialization.
     */
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()

    /**
     * The barcode scanner instance that tries to process the received images as barcodes.
     */
    private val scanner = BarcodeScanning.getClient(options)
    //endregion

    //region ImageAnalysis.Analyzer
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
            scanner.process(inputImage).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    barcodesReceivedListener.onBarcodesReceived(task.result)
                }
                imageProxy.close()
            }
        }
    }
    //endregion
}