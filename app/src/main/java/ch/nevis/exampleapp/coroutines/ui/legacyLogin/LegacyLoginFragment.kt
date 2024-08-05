/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.legacyLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ch.nevis.exampleapp.coroutines.databinding.FragmentLegacyLoginBinding
import ch.nevis.exampleapp.coroutines.domain.model.response.LoginResponse
import ch.nevis.exampleapp.coroutines.domain.model.response.Response
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import dagger.hilt.android.AndroidEntryPoint
import java.net.PasswordAuthentication

/**
 * [androidx.fragment.app.Fragment] implementation of Legacy Login view where the user
 * can enter a username and password and send it to start a legacy login process. If the login was
 * successful an in-band registration operation is started automatically.
 *
 * @constructor Creates a new instance.
 */
@AndroidEntryPoint
class LegacyLoginFragment : ResponseObserverFragment() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentLegacyLoginBinding? = null
    private val binding get() = _binding!!

    /**
     * View model implementation of this view.
     */
    override val viewModel: LegacyLoginViewModel by viewModels()
    //endregion

    //region Fragment
    /** @suppress */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLegacyLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    /** @suppress */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            val username =
                binding.usernameTextInputEditText.text?.toString() ?: return@setOnClickListener
            val password = binding.passwordTextInputEditText.text?.toList()?.toCharArray()
                ?: return@setOnClickListener
            viewModel.legacyLogin(PasswordAuthentication(username, password))
        }
    }

    /** @suppress */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region ResponseObserverFragment
    override fun processResponse(response: Response) {
        when (response) {
            is LoginResponse -> {
                viewModel.register(response.extId, response.cookies)
            }
            else -> super.processResponse(response)
        }
    }
    //endregion
}
