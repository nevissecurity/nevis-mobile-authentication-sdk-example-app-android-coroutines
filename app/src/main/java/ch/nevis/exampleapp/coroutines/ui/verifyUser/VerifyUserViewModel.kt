/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.verifyUser

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyBiometricUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyDevicePasscodeUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyFingerprintUseCase
import ch.nevis.exampleapp.coroutines.ui.base.CancellableOperationViewModel
import ch.nevis.exampleapp.coroutines.ui.verifyUser.model.VerifyUserViewMode
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricPromptOptions
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodePromptOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model implementation for Verify User view.
 *
 * @constructor Creates a new instance.
 * @param verifyBiometricUseCase An instance of a [VerifyBiometricUseCase] implementation.
 * @param verifyDevicePasscodeUseCase An instance of a [VerifyDevicePasscodeUseCase] implementation.
 * @param verifyFingerprintUseCase An instance of a [VerifyFingerprintUseCase] implementation.
 */
@HiltViewModel
class VerifyUserViewModel @Inject constructor(
    private val verifyBiometricUseCase: VerifyBiometricUseCase,
    private val verifyDevicePasscodeUseCase: VerifyDevicePasscodeUseCase,
    private val verifyFingerprintUseCase: VerifyFingerprintUseCase
) : CancellableOperationViewModel() {

    //region Properties
    /**
     * The mode, the Verify User view intend to be used/initialized.
     */
    private lateinit var verifyUserViewMode: VerifyUserViewMode

    /**
     * [BiometricPromptOptions] object that is required in case of biometric user verification for
     * the dialog shown by the OS.
     */
    private lateinit var biometricPromptOptions: BiometricPromptOptions

    /**
     * [DevicePasscodePromptOptions] object that is required in case of device passcode user verification for
     * the dialog shown by the OS.
     */
    private lateinit var devicePasscodePromptOptions: DevicePasscodePromptOptions
    //endregion

    //region Public Interface
    /**
     * Sets the current [VerifyUserViewMode].
     *
     * @param verifyUserViewMode The current [VerifyUserViewMode].
     */
    fun setVerifyUserViewMode(verifyUserViewMode: VerifyUserViewMode) {
        this.verifyUserViewMode = verifyUserViewMode
    }

    /**
     * Sets the biometric prompt options.
     *
     * @param biometricPromptOptions The biometric prompt options that is used for displaying the
     * dialog that asks the user to verify her-/himself.
     */
    fun setBiometricPromptOptions(biometricPromptOptions: BiometricPromptOptions) {
        this.biometricPromptOptions = biometricPromptOptions
    }

    /**
     * Sets the device passcode prompt options.
     *
     * @param devicePasscodePromptOptions The device passcode prompt options that is used
     * for displaying the dialog that asks the user to verify her-/himself.
     */
    fun setDevicePasscodePromptOptions(devicePasscodePromptOptions: DevicePasscodePromptOptions) {
        this.devicePasscodePromptOptions = devicePasscodePromptOptions
    }

    /**
     * Verifies the user using the previously selected authentication method.
     */
    fun verifyUser() {
        when (verifyUserViewMode) {
            VerifyUserViewMode.FINGERPRINT -> verifyFingerprint()
            VerifyUserViewMode.BIOMETRIC -> verifyBiometric()
            VerifyUserViewMode.DEVICE_PASSCODE -> verifyDevicePasscode()
        }
    }
    //endregion

    //region Private Interface
    /**
     * Starts fingerprint authentication for an operation.
     */
    private fun verifyFingerprint() {
        viewModelScope.launch(errorHandler) {
            val operationResponse = verifyFingerprintUseCase.execute()
            mutableResponseLiveData.postValue(operationResponse)
        }
    }

    /**
     * Starts biometric authentication for an operation.
     */
    private fun verifyBiometric() {
        viewModelScope.launch(errorHandler) {
            val operationResponse = verifyBiometricUseCase.execute(biometricPromptOptions)
            mutableResponseLiveData.postValue(operationResponse)
        }
    }

    /**
     * Starts device passcode authentication for an operation.
     */
    private fun verifyDevicePasscode() {
        viewModelScope.launch(errorHandler) {
            val operationResponse = verifyDevicePasscodeUseCase.execute(devicePasscodePromptOptions)
            mutableResponseLiveData.postValue(operationResponse)
        }
    }
    //endregion
}
