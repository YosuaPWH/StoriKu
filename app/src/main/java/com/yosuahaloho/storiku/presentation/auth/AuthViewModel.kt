package com.yosuahaloho.storiku.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yosuahaloho.storiku.domain.model.LoginRequest
import com.yosuahaloho.storiku.domain.model.RegisterRequest
import com.yosuahaloho.storiku.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Yosua on 16/04/2023
 */
@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepository) : ViewModel() {

    fun register(request: RegisterRequest) = repo.register(request).asLiveData()

    fun login(request: LoginRequest) = repo.login(request).asLiveData()
}