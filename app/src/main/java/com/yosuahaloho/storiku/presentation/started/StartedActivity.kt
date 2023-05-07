package com.yosuahaloho.storiku.presentation.started

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yosuahaloho.storiku.databinding.ActivityStartedBinding
import com.yosuahaloho.storiku.presentation.auth.LoginActivity
import com.yosuahaloho.storiku.presentation.auth.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.btnToRegisterPage.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnToLoginPage.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}