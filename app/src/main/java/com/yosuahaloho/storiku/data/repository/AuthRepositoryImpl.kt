package com.yosuahaloho.storiku.data.repository

import com.google.gson.Gson
import com.yosuahaloho.storiku.data.remote.ApiAuth
import com.yosuahaloho.storiku.data.remote.response.RegisterResponse
import com.yosuahaloho.storiku.data.remote.response.login.LoginResponse
import com.yosuahaloho.storiku.domain.model.LoginRequest
import com.yosuahaloho.storiku.domain.model.RegisterRequest
import com.yosuahaloho.storiku.domain.repository.AuthRepository
import com.yosuahaloho.storiku.domain.repository.UserDataStoreRepository
import com.yosuahaloho.storiku.utils.Result
import com.yosuahaloho.storiku.utils.getError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Yosua on 16/04/2023
 */
class AuthRepositoryImpl (private val api: ApiAuth, private val userStore: UserDataStoreRepository) : AuthRepository {

    override fun login(request: LoginRequest): Flow<Result<LoginResponse?>> = flow {
        emit(Result.Loading)
        try {
            api.login(request).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    val user = body?.loginResult!!
                    userStore.setDataUser(user)
                    emit(Result.Success(body))
                } else {
                    val errorMessage = it.getError<LoginResponse>().message
                    emit(Result.Error(errorMessage))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.Default)

    override fun register(request: RegisterRequest): Flow<Result<RegisterResponse?>> = flow {
        emit(Result.Loading)
        try {
            api.register(request).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    emit(Result.Success(body))
                } else {
                    val errorMessage = it.getError<RegisterResponse>().message
                    emit(Result.Error(errorMessage))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.Default)
}