package com.yosuahaloho.storiku.domain

import com.yosuahaloho.storiku.data.model.RequestLogin
import com.yosuahaloho.storiku.data.model.RequestRegister
import com.yosuahaloho.storiku.data.remote.response.login.LoginResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yosua on 16/04/2023
 */
interface AuthRepository {
    suspend fun login(request: RequestLogin) : T
    suspend fun register(request: RequestRegister)
}