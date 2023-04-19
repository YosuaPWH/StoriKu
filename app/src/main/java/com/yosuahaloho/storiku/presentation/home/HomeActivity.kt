package com.yosuahaloho.storiku.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container_view) as NavHostFragment
        navHostFragment.navController.setGraph(R.navigation.main_navigation)
    }
}