/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.authCloudRegistration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ch.nevis.exampleapp.coroutines.databinding.FragmentAuthCloudRegistrationBinding
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * [androidx.fragment.app.Fragment] implementation of Auth Cloud API Registration view where the user
 * can enter an enroll response or an app link URI and send it to the Auth Cloud API as input for
 * a registration operation.
 */
@AndroidEntryPoint
class AuthCloudRegistrationFragment : ResponseObserverFragment() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentAuthCloudRegistrationBinding? = null
    private val binding get() = _binding!!

    /**
     * View model implementation of this view.
     */
    override val viewModel: AuthCloudRegistrationViewModel by viewModels()
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthCloudRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            viewModel.authCloudRegistration(
                binding.enrollResponseTextInputEditText.text.toString(),
                binding.appLinkUriTextInputEditText.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion
}