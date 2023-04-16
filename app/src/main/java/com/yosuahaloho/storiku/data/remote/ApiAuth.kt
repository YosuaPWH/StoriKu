package com.yosuahaloho.storiku.data.remote

import com.yosuahaloho.storiku.data.model.RequestLogin
import com.yosuahaloho.storiku.data.model.RequestRegister
import com.yosuahaloho.storiku.data.remote.response.RegisterResponse
import com.yosuahaloho.storiku.data.remote.response.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

/**
 * Created by Yosua on 17/04/2023
 */
interface ApiAuth {

    @POST("register")
    fun register(
        @Body requestRegister: RequestRegister
    ): Response<RegisterResponse>

    @POST("login")
    fun login(
        @Body requestLogin: RequestLogin
    ): Response<LoginResponse>
}