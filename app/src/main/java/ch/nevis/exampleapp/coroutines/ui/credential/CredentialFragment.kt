/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.credential

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ch.nevis.exampleapp.coroutines.databinding.FragmentCredentialBinding
import ch.nevis.exampleapp.coroutines.domain.model.response.ChangePasswordResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.ChangePinResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.EnrollPasswordResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.EnrollPinResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyPasswordResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyPinResponse
import ch.nevis.exampleapp.coroutines.ui.base.CancelOperationOnBackPressedCallback
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import ch.nevis.exampleapp.coroutines.ui.credential.model.CredentialProtectionInformation
import ch.nevis.mobile.sdk.api.operation.RecoverableError
import dagger.hilt.android.AndroidEntryPoint

/**
 * [androidx.fragment.app.Fragment] implementation of Credential view where the user
 * can set, change and verify credential (PIN or password).
 */
@AndroidEntryPoint
class CredentialFragment : ResponseObserverFragment() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentCredentialBinding? = null
    private val binding get() = _binding!!

    /**
     * View model implementation of this view.
     */
    override val viewModel: CredentialViewModel by viewModels()

    /**
     * Safe Args navigation arguments.
     */
    private val navigationArguments: CredentialFragmentArgs by navArgs()

    /**
     * A [CountDownTimer] instance that is used to disable the screen for a cool down time period if necessary.
     */
    private var timer: CountDownTimer? = null
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCredentialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setCredentialViewMode(navigationArguments.parameter.credentialViewMode)
        viewModel.setCredentialType(navigationArguments.parameter)

        binding.titleTextView.setText(viewModel.title())
        binding.descriptionTextView.setText(viewModel.description())
        updateTextViews()
        binding.confirmButton.setOnClickListener {
            viewModel.confirm(
                binding.oldCredentialTextInputEditText.text?.toList()?.toCharArray() ?: charArrayOf(),
                binding.credentialTextInputEditText.text?.toList()?.toCharArray() ?: charArrayOf()
            )
        }

        updateUI(
            navigationArguments.parameter.lastRecoverableError,
            viewModel.protectionInfo(navigationArguments.parameter)
        )

        val callback = object : CancelOperationOnBackPressedCallback(viewModel) {
            override fun handleOnBackPressed() {
                timer?.cancel()
                timer = null
                super.handleOnBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region ResponseObserverFragment
    override fun processResponse(response: Response) {
        var lastRecoverableError: RecoverableError? = null
        var credentialProtectionInformation: CredentialProtectionInformation? = null

        when (response) {
            is ChangePinResponse -> {
                lastRecoverableError = response.lastRecoverableError
                credentialProtectionInformation = viewModel.pinProtectionInfo(response.pinAuthenticatorProtectionStatus)
            }
            is EnrollPinResponse -> {
                lastRecoverableError = response.lastRecoverableError
            }
            is VerifyPinResponse -> {
                lastRecoverableError = response.lastRecoverableError
                credentialProtectionInformation = viewModel.pinProtectionInfo(response.pinAuthenticatorProtectionStatus)
            }
            is ChangePasswordResponse -> {
                lastRecoverableError = response.lastRecoverableError
                credentialProtectionInformation = viewModel.passwordProtectionInfo(response.passwordAuthenticatorProtectionStatus)
            }
            is EnrollPasswordResponse -> {
                lastRecoverableError = response.lastRecoverableError
            }
            is VerifyPasswordResponse -> {
                lastRecoverableError = response.lastRecoverableError
                credentialProtectionInformation = viewModel.passwordProtectionInfo(response.passwordAuthenticatorProtectionStatus)
            }
            else -> super.processResponse(response)
        }

        updateUI(lastRecoverableError, credentialProtectionInformation)
    }
    //endregion

    //region Private Interface
    /**
     * Clears all error messages on screen.
     */
    private fun clearErrors() {
        binding.oldCredentialTextInputLayout.error = ""
        binding.credentialTextInputLayout.error = ""
    }

    private fun updateTextViews() {
        binding.oldCredentialTextInputLayout.visibility = viewModel.oldCredentialVisibility()
        binding.oldCredentialTextInputLayout.setHint(viewModel.oldCredentialTextFieldHint())
        binding.credentialTextInputLayout.setHint(viewModel.credentialTextFieldHint())
        binding.oldCredentialTextInputEditText.inputType = viewModel.textFieldInputType()
        binding.credentialTextInputEditText.inputType = viewModel.textFieldInputType()

        // Set on text changed listeners.
        binding.oldCredentialTextInputEditText.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            clearErrors()
        })

        binding.credentialTextInputEditText.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            clearErrors()
        })
    }

    /**
     * Updates the UI based on the received PIN authenticator status.
     *
     * @param lastRecoverableError The last recoverable error. It exists only if there was already a failed PIN operation attempt.
     * @param credentialProtectionInformation The authenticator protection information.
     */
    private fun updateUI(
        lastRecoverableError: RecoverableError?,
        credentialProtectionInformation: CredentialProtectionInformation?
    ) {
        lastRecoverableError?.let {
            var errorMessage = it.description()
            if (it.cause().isPresent) {
                errorMessage += " " + it.cause().get().message

            }

            binding.credentialTextInputLayout.error = errorMessage
        }

        binding.messageTextView.visibility = View.GONE
        credentialProtectionInformation?.let { protectionInformation ->
            protectionInformation.message.let {
                binding.messageTextView.visibility = View.VISIBLE
                binding.messageTextView.text = it
            }
            setViewState(!protectionInformation.isLocked)
            protectionInformation.remainingRetries?.let { remainingRetries ->
                updateMessage(
                    remainingRetries,
                    protectionInformation.coolDownTime
                )
                if (protectionInformation.coolDownTime > 0) {
                    startCoolDownTimer(
                        remainingRetries,
                        protectionInformation.coolDownTime
                    )
                }
            }
        }
    }

    /**
     * Enables or disables the UI components of this view.
     *
     * @param isEnabled A flag that tells whether the UI components should be enabled.
     */
    private fun setViewState(isEnabled: Boolean) {
        binding.oldCredentialTextInputLayout.isEnabled = isEnabled
        binding.credentialTextInputLayout.isEnabled = isEnabled
        binding.confirmButton.isEnabled = isEnabled
    }

    /**
     * Updates the text of message text view.
     *
     * @param remainingRetries The number of remaining retries available.
     * @param coolDownTime The time that must be passed before the user can try to provide credentials again.
     */
    private fun updateMessage(remainingRetries: Int, coolDownTime: Long) {
        binding.messageTextView.visibility = View.VISIBLE
        binding.messageTextView.text = viewModel.message(remainingRetries, coolDownTime)
    }

    /**
     * Starts the cool down timer.
     *
     * @param remainingRetries The number of remaining retries available.
     * @param coolDownTime The time that must be passed before the user can try to provide credentials again.
     */
    private fun startCoolDownTimer(remainingRetries: Int, coolDownTime: Long) {
        if (timer != null) {
            return
        }

        timer = object : CountDownTimer(
            coolDownTime * 1000,
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                updateMessage(remainingRetries, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                setViewState(true)
                updateMessage(remainingRetries, 0)
            }
        }.start()
    }
    //endregion
}
