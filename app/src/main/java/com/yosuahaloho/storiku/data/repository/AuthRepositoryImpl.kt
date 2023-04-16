package com.yosuahaloho.storiku.data.repository

import android.util.Log
import com.google.gson.Gson
import com.yosuahaloho.storiku.domain.model.LoginRequest
import com.yosuahaloho.storiku.domain.model.RegisterRequest
import com.yosuahaloho.storiku.data.remote.ApiAuth
import com.yosuahaloho.storiku.data.remote.response.RegisterResponse
import com.yosuahaloho.storiku.data.remote.response.login.LoginResponse
import com.yosuahaloho.storiku.domain.repository.AuthRepository
import com.yosuahaloho.storiku.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by Yosua on 16/04/2023
 */
class AuthRepositoryImpl @Inject constructor(private val api: ApiAuth) : AuthRepository {

    override fun login(request: LoginRequest) = flow {
        emit(Result.Loading)
        try {
            api.login(request).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    emit(Result.Success(body))
                } else {
                    val errorMessage =
                        Gson().fromJson(it.errorBody()?.charStream(), LoginResponse::class.java)
                    emit(Result.Error(errorMessage.message))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.Default)

    override fun register(request: RegisterRequest) = flow {
        emit(Result.Loading)
        try {
            api.register(request).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    emit(Result.Success(body))
                } else {
                    val errorMessage =
                        Gson().fromJson(it.errorBody()?.charStream(), RegisterResponse::class.java)
                    emit(Result.Error(errorMessage.message))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.Default)
}