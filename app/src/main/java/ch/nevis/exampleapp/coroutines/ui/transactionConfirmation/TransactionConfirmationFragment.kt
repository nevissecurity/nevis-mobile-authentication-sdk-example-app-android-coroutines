/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.transactionConfirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ch.nevis.exampleapp.coroutines.databinding.FragmentTransactionConfirmationBinding
import ch.nevis.exampleapp.coroutines.ui.base.CancelOperationOnBackPressedCallback
import ch.nevis.exampleapp.coroutines.ui.base.ResponseObserverFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * [androidx.fragment.app.Fragment] implementation of Transaction Confirmation view where the user
 * can allow or deny further processing of the operation based on the displayed transaction data.
 */
@AndroidEntryPoint
class TransactionConfirmationFragment : ResponseObserverFragment() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentTransactionConfirmationBinding? = null
    private val binding get() = _binding!!

    /**
     * View model implementation of this view.
     */
    override val viewModel: TransactionConfirmationViewModel by viewModels()

    /**
     * Safe Args navigation arguments.
     */
    private val navigationArguments: TransactionConfirmationFragmentArgs by navArgs()
    //endregion

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            viewModel.confirm(
                navigationArguments.parameter.operation, navigationArguments.parameter.accounts
            )
        }

        binding.cancelButton.setOnClickListener {
            viewModel.deny()
        }

        binding.transactionConfirmationDataTextView.text =
            navigationArguments.parameter.transactionConfirmationData

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
}