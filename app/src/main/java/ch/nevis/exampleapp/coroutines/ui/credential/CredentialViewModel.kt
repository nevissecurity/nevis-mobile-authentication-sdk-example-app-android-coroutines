/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.credential

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.domain.model.error.BusinessException
import ch.nevis.exampleapp.coroutines.domain.model.sdk.PasswordAuthenticatorProtectionStatusLastAttemptFailedImpl
import ch.nevis.exampleapp.coroutines.domain.model.sdk.PinAuthenticatorProtectionStatusLastAttemptFailedImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangePasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.util.message
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangePinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.SetPasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.SetPinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPinUseCase
import ch.nevis.exampleapp.coroutines.timber.sdk
import ch.nevis.exampleapp.coroutines.ui.base.CancellableOperationViewModel
import ch.nevis.exampleapp.coroutines.ui.credential.model.CredentialProtectionInformation
import ch.nevis.exampleapp.coroutines.ui.credential.model.CredentialViewMode
import ch.nevis.exampleapp.coroutines.ui.credential.parameter.CredentialNavigationParameter
import ch.nevis.exampleapp.coroutines.ui.credential.parameter.PasswordNavigationParameter
import ch.nevis.exampleapp.coroutines.ui.credential.parameter.PinNavigationParameter
import ch.nevis.mobile.sdk.api.localdata.Authenticator
import ch.nevis.mobile.sdk.api.operation.password.PasswordAuthenticatorProtectionStatus
import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * View model implementation of Credential view.
 */
@HiltViewModel
class CredentialViewModel @Inject constructor(
    /**
     * An Android [Context] object for [String] resource resolving.
     */
    @ApplicationContext
    private val context: Context,

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
    private val verifyPinUseCase: VerifyPinUseCase,

    /**
     * An instance of a [ChangePasswordUseCase] implementation.
     */
    private val changePasswordUseCase: ChangePasswordUseCase,

    /**
     * An instance of a [SetPasswordUseCase] implementation.
     */
    private val setPasswordUseCase: SetPasswordUseCase,

    /**
     * An instance of a [VerifyPasswordUseCase] implementation.
     */
    private val verifyPasswordUseCase: VerifyPasswordUseCase
) : CancellableOperationViewModel() {

    /**
     * The mode, the Credential view intend to be used/initialized.
     */
    private lateinit var credentialViewMode: CredentialViewMode

    /**
     * The type of the credential.
     */
    private lateinit var credentialType: String

    //region Public Interface
    /**
     * Sets the current [CredentialViewMode].
     *
     * @param credentialViewMode The current [CredentialViewMode].
     */
    fun setCredentialViewMode(credentialViewMode: CredentialViewMode) {
        this.credentialViewMode = credentialViewMode
    }

    /**
     * Sets the current credential type.
     *
     * @param parameter The navigation parameter.
     */
    fun setCredentialType(parameter: CredentialNavigationParameter) {
        this.credentialType = when (parameter) {
            is PinNavigationParameter -> Authenticator.PIN_AUTHENTICATOR_AAID
            is PasswordNavigationParameter -> Authenticator.PASSWORD_AUTHENTICATOR_AAID
            else -> throw BusinessException.invalidState()
        }
    }

    /**
     * Gets title string resource identifier based on the current [CredentialViewMode].
     *
     * @return The title string resource identifier.
     */
    @StringRes
    fun title(): Int {
        return when (credentialViewMode) {
            CredentialViewMode.CHANGE -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> R.string.pin_title_change
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> R.string.password_title_change
                else -> throw BusinessException.invalidState()
            }
            CredentialViewMode.ENROLLMENT -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> R.string.pin_title_enrollment
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> R.string.password_title_enrollment
                else -> throw BusinessException.invalidState()
            }
            CredentialViewMode.VERIFICATION -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> R.string.pin_title_verify
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> R.string.password_title_verify
                else -> throw BusinessException.invalidState()
            }
        }
    }

    /**
     * Gets description string resource identifier based on the current [CredentialViewMode].
     *
     * @return The description string resource identifier.
     */
    @StringRes
    fun description(): Int {
        return when (credentialViewMode) {
            CredentialViewMode.CHANGE -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> R.string.pin_description_change
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> R.string.password_description_change
                else -> throw BusinessException.invalidState()
            }
            CredentialViewMode.ENROLLMENT -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> R.string.pin_description_enrollment
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> R.string.password_description_enrollment
                else -> throw BusinessException.invalidState()
            }
            CredentialViewMode.VERIFICATION -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> R.string.pin_description_verify
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> R.string.password_description_verify
                else -> throw BusinessException.invalidState()
            }
        }
    }

    /**
     * Gets credential text field hint string resource identifier based on the current [CredentialViewMode].
     *
     * @return The credential text field hint string resource identifier.
     */
    @StringRes
    fun credentialTextFieldHint(): Int {
        return when (credentialType) {
            Authenticator.PIN_AUTHENTICATOR_AAID -> R.string.pin_hint_pin
            Authenticator.PASSWORD_AUTHENTICATOR_AAID -> R.string.password_hint_password
            else -> throw BusinessException.invalidState()
        }
    }

    /**
     * Gets old credential text field hint string resource identifier based on the current [CredentialViewMode].
     *
     * @return The old credential text field hint string resource identifier.
     */
    fun oldCredentialTextFieldHint(): Int {
        return when (credentialType) {
            Authenticator.PIN_AUTHENTICATOR_AAID -> R.string.pin_hint_old_pin
            Authenticator.PASSWORD_AUTHENTICATOR_AAID -> R.string.password_hint_old_password
            else -> throw IllegalStateException("Unsupported credential type.")
        }
    }

    /**
     * Gets visibility flag of old credential text field based on the current [CredentialViewMode].
     *
     * @return The visibility flag of old credential text field.
     */
    fun oldCredentialVisibility(): Int {
        return when (credentialViewMode) {
            CredentialViewMode.CHANGE -> View.VISIBLE
            else -> View.GONE
        }
    }

    /**
     * Gets input type of text field based on the current authenticator.
     *
     * @return The input type of text field.
     */
    fun textFieldInputType(): Int {
        return when (credentialType) {
            Authenticator.PIN_AUTHENTICATOR_AAID -> (InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_VARIATION_PASSWORD)
            Authenticator.PASSWORD_AUTHENTICATOR_AAID -> (InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD)
            else -> throw IllegalStateException("Unsupported credential type.")
        }
    }

    /**
     * Creates the credential protection information only if authenticator protection status is present.
     *
     * @param parameter The [CredentialNavigationParameter] that was received by the owner [CredentialFragment].
     * @return The created [CredentialProtectionInformation] instance.
     */
    fun protectionInfo(parameter: CredentialNavigationParameter): CredentialProtectionInformation? {
        return when (parameter) {
            is PinNavigationParameter -> {
                parameter.pinAuthenticatorProtectionStatus?.let {
                    pinProtectionInfo(it)
                }
            }
            is PasswordNavigationParameter -> {
                parameter.passwordAuthenticatorProtectionStatus?.let {
                    passwordProtectionInfo(it)
                }
            }
            else -> throw BusinessException.invalidState()
        }
    }

    /**
     * Creates PIN authenticator specific credential protection information.
     *
     * @param protectionStatus The [PinAuthenticatorProtectionStatus] that presented in the received [CredentialNavigationParameter].
     * @return The PIN authenticator specific [CredentialProtectionInformation] instance.
     */
    @SuppressLint("DefaultLocale")
    fun pinProtectionInfo(protectionStatus: PinAuthenticatorProtectionStatus): CredentialProtectionInformation {
        return when (protectionStatus) {
            is PinAuthenticatorProtectionStatus.Unlocked -> {
                Timber.asTree().sdk("PIN authenticator is unlocked.")
                CredentialProtectionInformation()
            }
            is PinAuthenticatorProtectionStatus.LastAttemptFailed -> {
                Timber.asTree().sdk("Last attempt failed using the PIN authenticator.")
                Timber.asTree()
                    .sdk(String.format(
                        "Remaining tries: %d, cool down period: %d",
                        protectionStatus.remainingRetries(),
                        protectionStatus.coolDownTimeInSeconds())
                    )
                CredentialProtectionInformation(
                    isLocked = protectionStatus.coolDownTimeInSeconds() > 0,
                    remainingRetries = protectionStatus.remainingRetries(),
                    coolDownTime = protectionStatus.coolDownTimeInSeconds(),
                )
            }
            is PinAuthenticatorProtectionStatus.LockedOut -> {
                Timber.asTree().sdk("PIN authenticator is locked.")
                CredentialProtectionInformation(
                    isLocked = true,
                    message = protectionStatus.message(context)
                )
            }
            else -> throw IllegalStateException("Unsupported PIN authenticator protection status.")
        }
    }

    /**
     * Creates Password authenticator specific credential protection information.
     *
     * @param protectionStatus The [PasswordAuthenticatorProtectionStatus] that presented in the received [CredentialNavigationParameter].
     * @return The Password authenticator specific [CredentialProtectionInformation] instance.
     */
    @SuppressLint("DefaultLocale")
    fun passwordProtectionInfo(protectionStatus: PasswordAuthenticatorProtectionStatus): CredentialProtectionInformation {
        return when (protectionStatus) {
            is PasswordAuthenticatorProtectionStatus.Unlocked -> {
                Timber.asTree().sdk("Password authenticator is unlocked.")
                CredentialProtectionInformation()
            }
            is PasswordAuthenticatorProtectionStatus.LastAttemptFailed -> {
                Timber.asTree().sdk("Last attempt failed using the Password authenticator.")
                Timber.asTree()
                    .sdk(String.format(
                        "Remaining tries: %d, cool down period: %d",
                        protectionStatus.remainingRetries(),
                        protectionStatus.coolDownTimeInSeconds())
                    )
                CredentialProtectionInformation(
                    isLocked = protectionStatus.coolDownTimeInSeconds() > 0,
                    remainingRetries = protectionStatus.remainingRetries(),
                    coolDownTime = protectionStatus.coolDownTimeInSeconds(),
                )
            }
            is PasswordAuthenticatorProtectionStatus.LockedOut -> {
                Timber.asTree().sdk("Password authenticator is locked.")
                CredentialProtectionInformation(
                    isLocked = true,
                    message = protectionStatus.message(context)
                )
            }
            else -> throw IllegalStateException("Unsupported Password authenticator protection status.")
        }
    }

    /**
     * Gets authenticator protection information specific message.
     *
     * @param remainingRetries The number of remaining retries available.
     * @param coolDownTime The time that must be passed before the user can try to provide credentials again.
     * @return The authenticator protection information specific message.
     */
    fun message(remainingRetries: Int, coolDownTime: Long): String {
        return when (credentialType) {
            Authenticator.PIN_AUTHENTICATOR_AAID ->
                PinAuthenticatorProtectionStatusLastAttemptFailedImpl(
                    remainingRetries = remainingRetries,
                    coolDownTimeInSeconds = coolDownTime
                ).message(context)
            Authenticator.PASSWORD_AUTHENTICATOR_AAID ->
                PasswordAuthenticatorProtectionStatusLastAttemptFailedImpl(
                    remainingRetries = remainingRetries,
                    coolDownTimeInSeconds = coolDownTime
                ).message(context)
            else -> String()
        }
    }

    /**
     * Confirm the given credentials based on the current [CredentialViewMode].
     *
     * @param oldCredential The text entered into old old credential text field. It is only used when
     *   the current [CredentialViewMode] is [CredentialViewMode.CHANGE] otherwise it will be ignored.
     * @param credential The text entered into credential text field.
     */
    fun confirm(oldCredential: CharArray, credential: CharArray) {
        when (credentialViewMode) {
            CredentialViewMode.CHANGE -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> changePin(oldCredential, credential)
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> changePassword(oldCredential, credential)
                else -> throw BusinessException.invalidState()
            }
            CredentialViewMode.ENROLLMENT -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> setPin(credential)
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> setPassword(credential)
                else -> throw BusinessException.invalidState()
            }
            CredentialViewMode.VERIFICATION -> when (credentialType) {
                Authenticator.PIN_AUTHENTICATOR_AAID -> verifyPin(credential)
                Authenticator.PASSWORD_AUTHENTICATOR_AAID -> verifyPassword(credential)
                else -> throw BusinessException.invalidState()
            }
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
     * Sets/creates a new PIN during a registration operation.
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

    /**
     * Changes the previously registered password.
     *
     * @param oldPassword The old password to be verified before changing it.
     * @param newPassword The new password.
     */
    private fun changePassword(oldPassword: CharArray, newPassword: CharArray) {
        viewModelScope.launch(errorHandler) {
            val response = changePasswordUseCase.execute(oldPassword, newPassword)
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Sets/creates a new password during a registration operation.
     *
     * @param password The password entered by the user.
     */
    private fun setPassword(password: CharArray) {
        viewModelScope.launch(errorHandler) {
            val response = setPasswordUseCase.execute(password)
            mutableResponseLiveData.postValue(response)
        }
    }

    /**
     * Verifies the given password.
     *
     * @param password The password entered by the user.
     */
    private fun verifyPassword(password: CharArray) {
        viewModelScope.launch(errorHandler) {
            val response = verifyPasswordUseCase.execute(password)
            mutableResponseLiveData.postValue(response)
        }
    }
    //endregion
}
