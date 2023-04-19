package com.yosuahaloho.storiku.presentation.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.ActivityLoginBinding
import com.yosuahaloho.storiku.domain.model.LoginRequest
import com.yosuahaloho.storiku.presentation.home.HomeActivity
import com.yosuahaloho.storiku.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        setupView()
    }

    private fun setupView() {
        loginBinding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        loginBinding.apply {
            val request = LoginRequest(
                email = edLoginEmail.text.toString(),
                password = edLoginPassword.text.toString()
            )
            viewModel.login(request).observe(this@LoginActivity) {
                when (it) {
                    is Result.Success -> {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    }
                    is Result.Loading -> {

                    }
                    is Result.Error -> {
                        Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}