package com.yosuahaloho.storiku.presentation.home

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container_view) as NavHostFragment
        navHostFragment.navController.setGraph(R.navigation.main_navigation)


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = navHostFragment.navController.currentDestination
                val currentPosition = navHostFragment.childFragmentManager.fragments[0]
                val previousFragment =
                    navHostFragment.navController.previousBackStackEntry?.destination
                if (currentFragment != null &&
                    previousFragment != null &&
                    currentFragment.id == R.id.listStoryFragment &&
                    previousFragment.id == R.id.addStoryFragment
                ) {
                    finish()
                } else if (currentFragment?.id == R.id.listStoryFragment) {
                    finish()
                } else {
                    navHostFragment.navController.navigateUp()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }



}