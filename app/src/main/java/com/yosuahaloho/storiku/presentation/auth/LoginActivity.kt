package com.yosuahaloho.storiku.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
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

        supportActionBar?.title = resources.getString(R.string.login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupView()
    }

    private fun setupView() {
        loginBinding.btnLogin.setOnClickListener {
            login()
        }

        loginBinding.edLoginEmail.addTextChangedListener {
            loginBinding.btnLogin.isEnabled = !loginBinding.layoutLoginEmail.isErrorEnabled
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
                        loginBinding.progressLayout.visibility = View.INVISIBLE
                        Toast.makeText(
                            this@LoginActivity,
                            resources.getString(R.string.success_login),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }

                    is Result.Loading -> {
                        loginBinding.progressLayout.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        loginBinding.progressLayout.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}