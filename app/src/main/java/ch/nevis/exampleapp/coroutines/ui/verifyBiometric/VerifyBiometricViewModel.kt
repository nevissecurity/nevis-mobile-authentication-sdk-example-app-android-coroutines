/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.verifyBiometric

import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyBiometricUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyFingerprintUseCase
import ch.nevis.exampleapp.coroutines.ui.base.CancellableOperationViewModel
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricPromptOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model implementation for Verify Biometric view.
 */
@HiltViewModel
class VerifyBiometricViewModel @Inject constructor(
    /**
     * An instance of a [VerifyBiometricUseCase] implementation.
     */
    private val verifyBiometricUseCase: VerifyBiometricUseCase,

    /**
     * An instance of a [VerifyFingerprintUseCase] implementation.
     */
    private val verifyFingerprintUseCase: VerifyFingerprintUseCase
) : CancellableOperationViewModel() {

    //region Public Interface
    /**
     * Starts fingerprint authentication for an operation.
     */
    fun verifyFingerprint() {
        viewModelScope.launch(errorHandler) {
            val operationResponse = verifyFingerprintUseCase.execute()
            mutableResponseLiveData.postValue(operationResponse)
        }
    }

    /**
     * Starts biometric authentication for an operation.
     *
     * @param biometricPromptOptions The biometric prompt options that is used for displaying the
     * dialog that asks the user to verify her-/himself.
     */
    fun verifyBiometric(biometricPromptOptions: BiometricPromptOptions) {
        viewModelScope.launch(errorHandler) {
            val operationResponse = verifyBiometricUseCase.execute(biometricPromptOptions)
            mutableResponseLiveData.postValue(operationResponse)
        }
    }
    //endregion
}