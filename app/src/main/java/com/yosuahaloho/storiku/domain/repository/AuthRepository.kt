package com.yosuahaloho.storiku.domain.repository

import com.yosuahaloho.storiku.domain.model.LoginRequest
import com.yosuahaloho.storiku.domain.model.RegisterRequest
import com.yosuahaloho.storiku.data.remote.response.RegisterResponse
import com.yosuahaloho.storiku.data.remote.response.login.LoginResponse
import com.yosuahaloho.storiku.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yosua on 16/04/2023
 */
interface AuthRepository {
    fun login(request: LoginRequest) : Flow<Result<LoginResponse?>>
    fun register(request: RegisterRequest) : Flow<Result<RegisterResponse?>>

    suspend fun logout() : Boolean
}