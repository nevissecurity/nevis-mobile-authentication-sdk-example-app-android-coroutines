/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.common.error.CancelErrorHandlerImpl
import ch.nevis.exampleapp.coroutines.common.error.DefaultErrorHandlerImpl
import ch.nevis.exampleapp.coroutines.common.error.ErrorHandlerChain
import ch.nevis.exampleapp.coroutines.databinding.ActivityMainBinding
import ch.nevis.exampleapp.coroutines.ui.util.navigateToHome
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * The main, start activity of the application. When the application starts this activity will be created and started.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //region Properties
    /**
     * UI component bindings.
     */
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    /**
     * View model instance for this activity.
     */
    private val viewModel: MainActivityViewModel by viewModels()

    /**
     * A [LogRecyclerViewAdapter] instance that is used to render SDK log items in a [androidx.recyclerview.widget.RecyclerView].
     */
    private lateinit var logRecyclerViewAdapter: LogRecyclerViewAdapter

    @Inject
    lateinit var errorHandlerChain: ErrorHandlerChain
    //endregion

    //region Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of Android Jetpack Navigation component and home screen/fragment.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationHostFragmentContainerView) as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.navigation_graph)

        val startDestinationId = R.id.homeFragment
        navGraph.setStartDestination(startDestinationId)
        navHostFragment.navController.graph = navGraph

        // OnClickListener implementation of toggle (show/hide) log button.
        binding.logToggleButton.setOnClickListener {
            if (binding.logRecyclerView.visibility == View.GONE) {
                binding.logRecyclerView.visibility = View.VISIBLE
                binding.logToggleButton.text = getString(R.string.main_hide_log)
            } else {
                binding.logRecyclerView.visibility = View.GONE
                binding.logToggleButton.text = getString(R.string.main_show_log)
            }
        }

        // Initialization of error handler chain.
        errorHandlerChain.removeAll()
        errorHandlerChain.add(CancelErrorHandlerImpl(navHostFragment.navController))
        errorHandlerChain.add(DefaultErrorHandlerImpl(baseContext, navHostFragment.navController))
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // Processing intent, the application may receive registration or authentication deeplinks here.
        processIntent(intent)
    }

    override fun onStart() {
        super.onStart()

        // Processing intent, the application may receive registration or authentication deeplinks here.
        processIntent(intent)

        // Initializing the recycler view that shows the SDK event log and its adapter.
        logRecyclerViewAdapter = LogRecyclerViewAdapter(this)
        binding.logRecyclerView.adapter = logRecyclerViewAdapter
        binding.logRecyclerView.layoutManager = LinearLayoutManager(this)

        // Observing log LiveData.
        viewModel.log.observe(this) {
            // Adding the new LogItem to the recycler view adapter.
            logRecyclerViewAdapter.addLogItem(it)
            binding.logRecyclerView.smoothScrollToPosition(logRecyclerViewAdapter.itemCount - 1)
        }
    }

    override fun onStop() {
        super.onStop()

        // Removing log LiveData observer.
        viewModel.log.removeObservers(this)
    }
    //endregion

    //region Private Interface
    /**
     * Processes the received [Intent]. This function checks if the intent has a [Intent.ACTION_VIEW] action and the data URI
     * contains `dispatchTokenResponse` query parameter. If yes, the application navigates to the home screen and passing
     * the value of the `dispatchTokenResponse` query parameter to it.
     */
    private fun processIntent(intent: Intent) {
        if (Intent.ACTION_VIEW != intent.action) {
            return
        }

        intent.data?.let { uri: Uri ->
            uri.getQueryParameter("dispatchTokenResponse")?.let { dispatchTokenResponse: String ->
                findNavController(R.id.navigationHostFragmentContainerView).navigateToHome(
                    dispatchTokenResponse
                )
            }
            // The intent data is processed only once.
            intent.data = null
        }
    }
    //endregion
}