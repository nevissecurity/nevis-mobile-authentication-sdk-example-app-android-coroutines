/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.verifyBiometric.parameter

import android.os.Parcelable
import ch.nevis.exampleapp.coroutines.ui.verifyBiometric.model.VerifyBiometricViewMode
import kotlinx.parcelize.Parcelize

/**
 * Navigation parameter data class for Verify Biometric view.
 */
@Parcelize
data class VerifyBiometricNavigationParameter(
    /**
     * The mode, the Verify Biometric view intend to be used/initialized.
     */
    val verifyBiometricViewMode: VerifyBiometricViewMode
) : Parcelable
