/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ch.nevis.exampleapp.coroutines.databinding.FragmentSelectAuthenticatorBinding
import ch.nevis.exampleapp.coroutines.ui.base.CancelOperationOnBackPressedCallback
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import ch.nevis.mobile.sdk.api.localdata.Authenticator
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment implementation of Select Authenticator view.
 *
 * This view renders the available authenticators as a list and the user
 * can select one of them. The selected authenticator will be sent back
 * to previous view in the backstack.
 */
@AndroidEntryPoint
class SelectAuthenticatorFragment : ResponseObserverFragment(),
    AuthenticatorSelectedListener {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentSelectAuthenticatorBinding? = null
    private val binding get() = _binding!!

    /**
     * The view model instance for this view.
     */
    override val viewModel: SelectAuthenticatorViewModel by viewModels()

    /**
     * Safe Args navigation arguments.
     */
    private val navigationArguments: SelectAuthenticatorFragmentArgs by navArgs()

    /**
     * An [AuthenticatorsRecyclerViewAdapter] instance that is used to render authenticators in a [androidx.recyclerview.widget.RecyclerView].
     */
    private lateinit var authenticatorsRecyclerViewAdapter: AuthenticatorsRecyclerViewAdapter
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectAuthenticatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context ?: return

        authenticatorsRecyclerViewAdapter = AuthenticatorsRecyclerViewAdapter(
            navigationArguments.parameter.authenticators.toTypedArray(),
            this
        )
        binding.authenticatorsRecyclerView.adapter = authenticatorsRecyclerViewAdapter
        binding.authenticatorsRecyclerView.layoutManager = LinearLayoutManager(context)

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

    //region AuthenticatorsRecyclerViewAdapterDelegate
    override fun onAuthenticatorSelected(authenticator: Authenticator) {
        viewModel.selectAuthenticator(authenticator.aaid())
    }
    //endregion
}