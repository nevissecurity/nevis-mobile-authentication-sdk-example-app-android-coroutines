/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.verifyUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.databinding.FragmentVerifyUserBinding
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.domain.model.response.VerifyFingerprintResponse
import ch.nevis.exampleapp.coroutines.ui.base.CancelOperationOnBackPressedCallback
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import ch.nevis.exampleapp.coroutines.ui.verifyUser.model.VerifyUserViewMode
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricPromptOptions
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodePromptOptions
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment implementation of Verify User view where the user can verify her-/himself with
 * fingerprint, face ID or device passcode.
 */
@AndroidEntryPoint
class VerifyUserFragment : ResponseObserverFragment() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentVerifyUserBinding? = null
    private val binding get() = _binding!!

    /**
     * The view model instance for this view.
     */
    override val viewModel: VerifyUserViewModel by viewModels()

    /**
     * Safe Args navigation arguments.
     */
    private val navigationArguments: VerifyUserFragmentArgs by navArgs()
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setVerifyUserViewMode(navigationArguments.parameter.verifyUserViewMode)
        viewModel.setBiometricPromptOptions(
            BiometricPromptOptions.builder()
                .title(getString(R.string.verify_user_biometric_prompt_title))
                .cancelButtonText(getString(R.string.verify_user_biometric_prompt_cancel_button_title))
                .build()
        )
        viewModel.setDevicePasscodePromptOptions(
            DevicePasscodePromptOptions.builder()
                .title(getString(R.string.verify_user_device_passcode_prompt_title))
                .build()
        )

        val title = getString(
            R.string.verify_user_title,
            getString(navigationArguments.parameter.authenticatorTitleResId)
        )
        binding.titleTextView.text = title

        binding.confirmButton.setOnClickListener {
            val visibility = when (navigationArguments.parameter.verifyUserViewMode) {
                VerifyUserViewMode.FINGERPRINT -> View.VISIBLE
                else -> View.GONE
            }
            binding.descriptionTextView.visibility = visibility
            viewModel.verifyUser()
        }

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

    //region OperationResponseObserverFragment
    override fun processResponse(response: Response) {
        when (response) {
            is VerifyFingerprintResponse -> {
                if (response.lastRecoverableError != null) {
                    binding.errorMessageTextView.text = response.lastRecoverableError.description()
                }
            }

            else -> super.processResponse(response)
        }
    }
    //endregion
}