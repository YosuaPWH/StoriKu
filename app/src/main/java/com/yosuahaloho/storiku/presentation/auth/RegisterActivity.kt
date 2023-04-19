package com.yosuahaloho.storiku.presentation.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
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

        setupView()
    }

    private fun setupView() {
        regisBinding.btnRegister.setOnClickListener {
            register()
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
                viewModel.register(request).observe(this@RegisterActivity) {
                    when (it) {
                        is Result.Success -> {
                            lifecycleScope.launch {
//                                delay(3000)
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }

                        is Result.Loading -> {

                        }

                        is Result.Error -> {
                            Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}