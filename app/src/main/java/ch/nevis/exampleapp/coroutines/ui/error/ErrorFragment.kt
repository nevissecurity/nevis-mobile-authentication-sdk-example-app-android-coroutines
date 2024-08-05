/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ch.nevis.exampleapp.coroutines.databinding.FragmentErrorBinding
import ch.nevis.exampleapp.coroutines.ui.util.navigateToHome
import dagger.hilt.android.AndroidEntryPoint

/**
 * [androidx.fragment.app.Fragment] implementation of Error view.
 *
 * @constructor Creates a new instance.
 */
@AndroidEntryPoint
class ErrorFragment : Fragment() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!

    /**
     * Safe Args navigation arguments.
     */
    private val navigationArguments: ErrorFragmentArgs by navArgs()
    //endregion

    //region Fragment
    /** @suppress */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    /** @suppress */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.descriptionTextView.text = navigationArguments.parameter.message

        binding.confirmButton.setOnClickListener {
            findNavController().navigateToHome()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateToHome()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    /** @suppress */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion
}
