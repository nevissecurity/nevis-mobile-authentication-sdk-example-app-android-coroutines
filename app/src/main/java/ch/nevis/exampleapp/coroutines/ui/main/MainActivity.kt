/*
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022-2026. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.ui.main

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import ch.nevis.exampleapp.coroutines.R
import ch.nevis.exampleapp.coroutines.common.error.CancelErrorHandlerImpl
import ch.nevis.exampleapp.coroutines.common.error.DefaultErrorHandlerImpl
import ch.nevis.exampleapp.coroutines.common.error.ErrorHandlerChain
import ch.nevis.exampleapp.coroutines.databinding.ActivityMainBinding
import ch.nevis.exampleapp.coroutines.ui.util.navigateToHome
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

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

    /**
     * An instance of an [ErrorHandlerChain] implementation.
     */
    @Inject
    lateinit var errorHandlerChain: ErrorHandlerChain

    /**
     * The insets of the system bars, the display cutout and the software keyboard that were dispatched to the root
     * view most recently. They are needed to re-apply the bottom inset to the correct view when the log is shown or
     * hidden.
     */
    private var windowInsets: Insets = Insets.NONE
    //endregion

    //region Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        // The app draws behind the system bars on all supported Android versions. The status bar is drawn over the App Bar,
        // with `colorPrimary` being its background color, so its icons should use the same light/dark appearance as `colorOnPrimary`.
        val colorOnPrimary = MaterialColors.getColor(
            this,
            com.google.android.material.R.attr.colorOnPrimary,
            Color.WHITE
        )
        enableEdgeToEdge(
            statusBarStyle = if (MaterialColors.isColorLight(colorOnPrimary)) {
                SystemBarStyle.dark(Color.TRANSPARENT)
            } else {
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
            }
        )
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        applyWindowInsets()

        // Initialization of Android Jetpack Navigation component and home screen/fragment.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationHostFragmentContainerView) as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.navigation_graph)
        navHostFragment.navController.graph = navGraph

        // OnClickListener implementation of toggle (show/hide) log button.
        binding.logToggleButton.setOnClickListener {
            if (binding.logRecyclerView.isGone) {
                binding.logRecyclerView.visibility = View.VISIBLE
                binding.logToggleButton.text = getString(R.string.main_hide_log)
            } else {
                binding.logRecyclerView.visibility = View.GONE
                binding.logToggleButton.text = getString(R.string.main_show_log)
            }
            applyBottomInset()
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

        // Observing log Flow.
        lifecycleScope.launch {
            viewModel.log.collect {
                // Adding the new LogItem to the recycler view adapter.
                logRecyclerViewAdapter.addLogItem(it)
                binding.logRecyclerView.smoothScrollToPosition(logRecyclerViewAdapter.itemCount - 1)
            }
        }
    }
    //endregion

    //region Private Interface

    /**
     * Applies the system bar, display cutout and software keyboard insets to the views of the activity, so that the
     * UI is drawn edge-to-edge but its content is neither covered by the system bars nor by the keyboard:
     * - the top inset is applied as padding to the app bar, so that it is drawn behind the status bar,
     * - the horizontal insets are applied as padding to the root view,
     * - the bottom inset is applied to the log views, see [applyBottomInset].
     */
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            windowInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout() or
                    WindowInsetsCompat.Type.ime()
            )
            view.updatePadding(left = windowInsets.left, right = windowInsets.right)
            binding.appBarLayout.updatePadding(top = windowInsets.top)
            applyBottomInset()
            WindowInsetsCompat.CONSUMED
        }
    }

    /**
     * Applies the bottom inset as padding to the view that is currently at the bottom of the screen: the log
     * list if it is visible, otherwise the log toggle button container.
     */
    private fun applyBottomInset() {
        val isLogVisible = binding.logRecyclerView.isVisible
        binding.logRecyclerView.updatePadding(bottom = if (isLogVisible) windowInsets.bottom else 0)
        binding.logToggleButtonContainer.updatePadding(bottom = if (isLogVisible) 0 else windowInsets.bottom)
    }

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
