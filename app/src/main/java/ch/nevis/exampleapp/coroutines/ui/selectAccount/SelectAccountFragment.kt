/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.selectAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ch.nevis.exampleapp.coroutines.databinding.FragmentSelectAccountBinding
import ch.nevis.exampleapp.coroutines.ui.base.CancelOperationOnBackPressedCallback
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import ch.nevis.mobile.sdk.api.localdata.Account
import dagger.hilt.android.AndroidEntryPoint

/**
 * [androidx.fragment.app.Fragment] implementation of Select Account view where the user
 * can one of her/his registered accounts for an operation.
 */
@AndroidEntryPoint
class SelectAccountFragment : ResponseObserverFragment(),
    AccountSelectedListener {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentSelectAccountBinding? = null
    private val binding get() = _binding!!

    /**
     * The view model instance for this view.
     */
    override val viewModel: SelectAccountViewModel by viewModels()

    /**
     * Safe Args navigation arguments.
     */
    private val navigationArguments: SelectAccountFragmentArgs by navArgs()

    /**
     * An [AccountsRecyclerViewAdapter] instance that is used to render accounts in a [androidx.recyclerview.widget.RecyclerView].
     */
    private lateinit var accountsRecyclerViewAdapter: AccountsRecyclerViewAdapter
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context ?: return

        accountsRecyclerViewAdapter = AccountsRecyclerViewAdapter(
            navigationArguments.parameter.accounts?.toTypedArray() ?: arrayOf(),
            this
        )
        binding.accountsRecyclerView.adapter = accountsRecyclerViewAdapter
        binding.accountsRecyclerView.layoutManager = LinearLayoutManager(context)

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

    //region AccountSelectedListener
    override fun onAccountSelected(account: Account) {
        viewModel.selectAccount(
            navigationArguments.parameter.operation,
            account.username()
        )
    }
    //endregion
}