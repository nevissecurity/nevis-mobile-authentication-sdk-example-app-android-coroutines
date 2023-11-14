/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ch.nevis.exampleapp.coroutines.NavigationGraphDirections
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.databinding.FragmentHomeBinding
import ch.nevis.exampleapp.coroutines.domain.model.response.GetAccountsResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.InitializeClientCompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.PayloadDecodeCompletedResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import ch.nevis.exampleapp.coroutines.ui.util.handleDispatchTokenResponse
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment implementation of home view. This view is the start/home view of the application.
 * It can handle an out-of-band payload or Auth Cloud API registration, in-band authentication,
 * in-band registration. change PIN, change device information, or deregistration can be started from here.
 */
@AndroidEntryPoint
class HomeFragment : ResponseObserverFragment() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    /**
     * View model for this view.
     */
    override val viewModel: HomeViewModel by viewModels()
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        binding.qrCodeButton.setOnClickListener {
            val action = NavigationGraphDirections.actionGlobalQrReaderFragment()
            navController.navigate(action)
        }

        binding.inBandAuthenticationButton.setOnClickListener {
            viewModel.inBandAuthentication()
        }

        binding.deregisterButton.setOnClickListener {
            viewModel.deregister()
        }

        binding.changePinButton.setOnClickListener {
            viewModel.changePin()
        }

        binding.changeDeviceInformationButton.setOnClickListener {
            val action = NavigationGraphDirections.actionGlobalChangeDeviceInformationFragment()
            navController.navigate(action)
        }

        binding.authCloudRegistrationButton.setOnClickListener {
            val action = NavigationGraphDirections.actionGlobalAuthCloudRegistrationFragment()
            navController.navigate(action)
        }

        binding.deleteAuthenticatorsButton.setOnClickListener {
            viewModel.deleteAuthenticators()
        }

        binding.inBandRegistrationButton.setOnClickListener {
            val action = NavigationGraphDirections.actionGlobalLegacyLoginFragment()
            navController.navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.initClient()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region OperationResponseObserverFragment
    override fun processResponse(response: Response) {
        when (response) {
            is InitializeClientCompletedResponse -> {
                viewModel.finishOperation()
                viewModel.initView()

                handleDispatchTokenResponse {
                    viewModel.decodeOutOfBandPayload(it)
                }
            }
            is GetAccountsResponse -> {
                binding.titleTextView.text = context?.getString(
                    R.string.home_registered_accounts,
                    response.accounts.size
                )
            }
            is PayloadDecodeCompletedResponse -> {
                viewModel.processOutOfBandPayload(response.payload)
            }
            else -> super.processResponse(response)
        }
    }
    //endregion
}