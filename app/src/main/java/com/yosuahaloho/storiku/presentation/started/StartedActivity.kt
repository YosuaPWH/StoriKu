package com.yosuahaloho.storiku.presentation.started

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.ActivityStartedBinding
import com.yosuahaloho.storiku.presentation.auth.LoginActivity
import com.yosuahaloho.storiku.presentation.auth.RegisterActivity

class StartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetstartedCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}