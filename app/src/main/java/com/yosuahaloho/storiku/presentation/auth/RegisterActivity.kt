package com.yosuahaloho.storiku.presentation.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yosuahaloho.storiku.databinding.ActivityRegisterBinding
import com.yosuahaloho.storiku.domain.model.RegisterRequest
import com.yosuahaloho.storiku.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var regisBinding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        regisBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(regisBinding.root)

        supportActionBar?.title = "Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupView()
    }

    private fun setupView() {
        regisBinding.btnRegister.setOnClickListener {
            register()
        }

        regisBinding.edRegisterEmail.isButtonEnabled()
        regisBinding.edRegisterPassword.isButtonEnabled()
    }

    private fun TextInputEditText.isButtonEnabled() {
        this.addTextChangedListener {
            regisBinding.apply {
                btnRegister.isEnabled = !layoutRegisterEmail.isErrorEnabled && !layoutRegisterPassword.isErrorEnabled
            }
        }
    }

    private fun register() {
        regisBinding.apply {
            if (edRegisterEmail.text.isNullOrEmpty() || edRegisterName.text.isNullOrEmpty() || edRegisterPassword.text.isNullOrEmpty()) {
                Toast.makeText(this@RegisterActivity, "Gak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                val request = RegisterRequest(
                    name = edRegisterName.text.toString(),
                    email = edRegisterEmail.text.toString(),
                    password = edRegisterPassword.text.toString()
                )
                lifecycleScope.launch {
                    viewModel.register(request).collect {
                        when (it) {
                            is Result.Success -> {
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                finish()
                            }

                            is Result.Loading -> {

                            }

                            is Result.Error -> {
                                Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("REGISTER", "REGISTER GAGAl ${it.error}")
                            }
                        }
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