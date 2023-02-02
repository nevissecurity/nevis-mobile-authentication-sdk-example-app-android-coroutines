/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.pin

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangePinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.SetPinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPinUseCase
import ch.nevis.exampleapp.coroutines.ui.base.CancellableOperationViewModel
import ch.nevis.exampleapp.coroutines.ui.pin.model.PinViewMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model implementation of PIN view.
 */
@HiltViewModel
class PinViewModel @Inject constructor(
    /**
     * An instance of a [ChangePinUseCase] implementation.
     */
    private val changePinUseCase: ChangePinUseCase,

    /**
     * An instance of a [SetPinUseCase] implementation.
     */
    private val setPinUseCase: SetPinUseCase,

    /**
     * An instance of a [VerifyPinUseCase] implementation.
     */
    private val verifyPinUseCase: VerifyPinUseCase
) : CancellableOperationViewModel() {

    private var pinViewMode = PinViewMode.ENROLL_PIN

    //region Public Interface
    fun setPinMode(pinViewMode: PinViewMode) {
        this.pinViewMode = pinViewMode
    }

    /**
     * Gets title string resource identifier based on the current [PinViewMode].
     *
     * @return The title string resource identifier.
     */
    @StringRes
    fun title(): Int {
        return when (pinViewMode) {
            PinViewMode.CHANGE_PIN -> R.string.pin_title_change_pin
            PinViewMode.ENROLL_PIN -> R.string.pin_title_create_pin
            PinViewMode.VERIFY_PIN -> R.string.pin_title_verify_pin
        }
    }

    /**
     * Gets message string resource identifier based on the current [PinViewMode].
     *
     * @return The message string resource identifier.
     */
    @StringRes
    fun message(): Int {
        return when (pinViewMode) {
            PinViewMode.CHANGE_PIN -> R.string.pin_message_change_pin
            PinViewMode.ENROLL_PIN -> R.string.pin_message_create_pin
            PinViewMode.VERIFY_PIN -> R.string.pin_message_verify_pin
        }
    }

    /**
     * Gets PIN text field hint string resource identifier based on the current [PinViewMode].
     *
     * @return The PIN text field hint string resource identifier.
     */
    @StringRes
    fun pinTextFieldHint(): Int {
        return when (pinViewMode) {
            PinViewMode.CHANGE_PIN -> R.string.pin_hint_new_pin
            else -> R.string.pin_hint_pin
        }
    }

    /**
     * Gets visibility flag of old PIN text field based on the current [PinViewMode].
     *
     * @return The visibility flag of old PIN text field.
     */
    fun oldPinVisibility(): Int {
        return when (pinViewMode) {
            PinViewMode.CHANGE_PIN -> View.VISIBLE
            else -> View.GONE
        }
    }

    /**
     * Processes the given PINs based on the current [PinViewMode].
     *
     * @param oldPin The text entered into old PIN text field. It is only used when the current [PinViewMode]
     * is [PinViewMode.CHANGE_PIN] otherwise it will be ignored.
     * @param pin The text entered into PIN text field.
     */
    fun processPins(oldPin: CharArray, pin: CharArray) {
        when (pinViewMode) {
            PinViewMode.CHANGE_PIN -> changePin(oldPin, pin)
            PinViewMode.ENROLL_PIN -> setPin(pin)
            PinViewMode.VERIFY_PIN -> verifyPin(pin)
        }
    }
    //endregion

    //region Private Interface
    /**
     * Changes the previously registered PIN.
     *
     * @param oldPin The old PIN to be verified before changing it.
     * @param newPin The new PIN.
     */
    private fun changePin(oldPin: CharArray, newPin: CharArray) {
        viewModelScope.launch(errorHandler) {
            val response = changePinUseCase.execute(oldPin, newPin)
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Sets/creates a new PIN during a registration opertation.
     *
     * @param pin The PIN entered by the user.
     */
    private fun setPin(pin: CharArray) {
        viewModelScope.launch(errorHandler) {
            val response = setPinUseCase.execute(pin)
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Verifies the given PIN.
     *
     * @param pin The PIN entered by the user.
     */
    private fun verifyPin(pin: CharArray) {
        viewModelScope.launch(errorHandler) {
            val response = verifyPinUseCase.execute(pin)
            mutableResponseLiveData.postValue(response)
        }
    }
    //endregion
}