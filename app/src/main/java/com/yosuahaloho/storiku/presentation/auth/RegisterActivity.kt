package com.yosuahaloho.storiku.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yosuahaloho.storiku.R
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

        supportActionBar?.title = resources.getString(R.string.register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupView()
    }

    private fun setupView() {
        regisBinding.btnRegister.setOnClickListener {
            register()
        }

        regisBinding.edRegisterEmail.isButtonEnabled()
        regisBinding.edRegisterPassword.isButtonEnabled()
        regisBinding.edRegisterName.isButtonEnabled()

        regisBinding.edRegisterName.doOnTextChanged { text, _, _, _ ->
            val parentLayout = regisBinding.edRegisterName.parent.parent as TextInputLayout
            if (text.isNullOrBlank()) {
                parentLayout.error = resources.getString(R.string.error_name_empty)
            } else {
                parentLayout.isErrorEnabled = false
            }
        }
        regisBinding.layoutRegisterPassword.isErrorEnabled = true
    }

    private fun TextInputEditText.isButtonEnabled() {
        this.addTextChangedListener {
            regisBinding.apply {
                btnRegister.isEnabled =
                    !layoutRegisterEmail.isErrorEnabled && !layoutRegisterPassword.isErrorEnabled && !layoutRegisterName.isErrorEnabled
            }
        }
    }

    private fun register() {
        regisBinding.apply {

            val request = RegisterRequest(
                name = edRegisterName.text.toString(),
                email = edRegisterEmail.text.toString(),
                password = edRegisterPassword.text.toString()
            )
            lifecycleScope.launch {
                viewModel.register(request).collect {
                    when (it) {
                        is Result.Success -> {
                            regisBinding.progressLayout.visibility = View.INVISIBLE
                            Toast.makeText(
                                this@RegisterActivity,
                                resources.getString(R.string.success_register),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@RegisterActivity,
                                    LoginActivity::class.java
                                )
                            )
                            finish()
                        }

                        is Result.Loading -> {
                            regisBinding.progressLayout.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            regisBinding.progressLayout.visibility = View.INVISIBLE
                            Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_SHORT)
                                .show()
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