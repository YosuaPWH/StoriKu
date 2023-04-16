package com.yosuahaloho.storiku.data.remote

import com.yosuahaloho.storiku.domain.model.LoginRequest
import com.yosuahaloho.storiku.domain.model.RegisterRequest
import com.yosuahaloho.storiku.data.remote.response.RegisterResponse
import com.yosuahaloho.storiku.data.remote.response.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Yosua on 17/04/2023
 */
interface ApiAuth {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}