/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.pin

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.databinding.FragmentPinBinding
import ch.nevis.exampleapp.coroutines.domain.model.response.ChangePinResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.EnrollPinResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyPinResponse
import ch.nevis.exampleapp.coroutines.ui.base.CancelOperationOnBackPressedCallback
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import ch.nevis.mobile.sdk.api.operation.RecoverableError
import ch.nevis.mobile.sdk.api.operation.pin.PinAuthenticatorProtectionStatus
import dagger.hilt.android.AndroidEntryPoint

/**
 * [androidx.fragment.app.Fragment] implementation of PIN view where the user
 * can set, change and verify PIN.
 */
@AndroidEntryPoint
class PinFragment : ResponseObserverFragment() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!

    /**
     * View model implementation of this view.
     */
    override val viewModel: PinViewModel by viewModels()

    /**
     * Safe Args navigation arguments.
     */
    private val navigationArguments: PinFragmentArgs by navArgs()

    /**
     * A [CountDownTimer] instance that is used to disable the screen for a cool down time period if necessary.
     */
    private var timer: CountDownTimer? = null

    /**
     * A boolean flag that tells whether the timer is started/running or not.
     */
    private var isTimerRunning = false
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setPinMode(navigationArguments.parameter.pinViewMode)

        binding.titleTextView.setText(viewModel.title())
        binding.messageTextView.setText(viewModel.message())
        binding.oldPinTextInputLayout.visibility = viewModel.oldPinVisibility()
        binding.pinTextInputLayout.setHint(viewModel.pinTextFieldHint())

        binding.confirmButton.setOnClickListener {
            viewModel.processPins(
                binding.oldPinTextInputEditText.text?.toList()?.toCharArray() ?: charArrayOf(),
                binding.pinTextInputEditText.text?.toList()?.toCharArray() ?: charArrayOf()
            )
        }

        // Set on text changed listeners.
        binding.oldPinTextInputEditText.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            clearErrors()
        })

        binding.pinTextInputEditText.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            clearErrors()
        })

        updateUI(
            navigationArguments.parameter.lastRecoverableError,
            navigationArguments.parameter.pinAuthenticatorProtectionStatus
        )

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            CancelOperationOnBackPressedCallback(viewModel)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region ResponseObserverFragment
    override fun processResponse(response: Response) {
        var lastRecoverableError: RecoverableError? = null
        var pinAuthenticatorProtectionStatus: PinAuthenticatorProtectionStatus? = null

        when (response) {
            is ChangePinResponse -> {
                lastRecoverableError = response.lastRecoverableError
                pinAuthenticatorProtectionStatus = response.pinAuthenticatorProtectionStatus
            }
            is EnrollPinResponse -> {
                lastRecoverableError = response.lastRecoverableError
            }
            is VerifyPinResponse -> {
                lastRecoverableError = response.lastRecoverableError
                pinAuthenticatorProtectionStatus = response.pinAuthenticatorProtectionStatus
            }
            else -> super.processResponse(response)
        }

        updateUI(lastRecoverableError, pinAuthenticatorProtectionStatus)
    }
    //endregion

    //region Private Interface
    /**
     * Clears all error messages on screen.
     */
    private fun clearErrors() {
        binding.oldPinTextInputLayout.error = ""
        binding.pinTextInputLayout.error = ""
    }

    /**
     * Updates the UI based on the received PIN authenticator status.
     *
     * @param lastRecoverableError The last recoverable error. It exists only if there was already a failed PIN operation attempt.
     * @param pinAuthenticatorProtectionStatus The PIN authenticator status.
     */
    private fun updateUI(
        lastRecoverableError: RecoverableError?,
        pinAuthenticatorProtectionStatus: PinAuthenticatorProtectionStatus?
    ) {
        lastRecoverableError?.let {
            binding.pinTextInputLayout.error = it.description()
        }

        when (pinAuthenticatorProtectionStatus) {
            // In case of cool down state the screen should be disabled for the specified cool down time period.
            is PinAuthenticatorProtectionStatus.LastAttemptFailed -> {
                setScreenEnabled(false, pinAuthenticatorProtectionStatus.coolDownTimeInSeconds())
                isTimerRunning = true
                timer = object : CountDownTimer(
                    pinAuthenticatorProtectionStatus.coolDownTimeInSeconds() * 1000,
                    1000
                ) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.messageTextView.text = context?.getString(
                            R.string.pin_message_cool_down,
                            millisUntilFinished / 1000
                        )
                    }

                    override fun onFinish() {
                        setScreenEnabled(true)
                        isTimerRunning = false
                    }
                }.start()
            }
            // In case of locked state, the screen will be disabled forever.
            is PinAuthenticatorProtectionStatus.LockedOut -> {
                setScreenEnabled(false)
            }
            else -> {
                // Do nothing.
            }
        }
    }

    /**
     * Enables or disables UI components of this view/screen.
     *
     * @param isEnabled A flag that tells the UI components should be enabled or disabled.
     * @param coolDownTime The cool down time period the UI components are disabled for. Please note this method
     * won't start a timer, this value is used only for information text composing.
     */
    private fun setScreenEnabled(isEnabled: Boolean, coolDownTime: Long? = null) {
        binding.oldPinTextInputLayout.isEnabled = isEnabled
        binding.pinTextInputLayout.isEnabled = isEnabled
        binding.confirmButton.isEnabled = isEnabled
        if (isEnabled) {
            binding.messageTextView.setText(viewModel.message())
        } else {
            binding.messageTextView.text = if (coolDownTime != null) {
                context?.getString(R.string.pin_message_cool_down, coolDownTime)
            } else {
                context?.getString(R.string.pin_message_locked_out)
            }
        }
    }
    //endregion
}