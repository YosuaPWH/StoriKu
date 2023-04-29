package com.yosuahaloho.storiku.presentation.routing

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.yosuahaloho.storiku.presentation.home.HomeActivity
import com.yosuahaloho.storiku.presentation.started.StartedActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by Yosua on 28/04/2023
 */
@AndroidEntryPoint
class RoutingActivity : ComponentActivity() {

    private val viewModel: RoutingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { true }
        lifecycleScope.launch {
            viewModel.getUser().collect {
                val intent = if (it != null) {
                    Intent(this@RoutingActivity, HomeActivity::class.java)
                } else {
                    Intent(this@RoutingActivity, StartedActivity::class.java)
                }
                finish()
                startActivity(intent)
            }
        }
    }
}