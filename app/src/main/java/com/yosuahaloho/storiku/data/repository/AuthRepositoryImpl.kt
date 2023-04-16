package com.yosuahaloho.storiku.data.repository

import com.yosuahaloho.storiku.data.model.RequestLogin
import com.yosuahaloho.storiku.data.model.RequestRegister
import com.yosuahaloho.storiku.data.remote.ApiService
import com.yosuahaloho.storiku.domain.AuthRepository
import com.yosuahaloho.storiku.utils.Result
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Yosua on 16/04/2023
 */
class AuthRepositoryImpl @Inject constructor(private val api: ApiService) : AuthRepository {

    override suspend fun login(request: RequestLogin) = flow {
        emit(Result.Loading)
        try {
            api.login(request).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    emit(Result.Success(body))
                } else {
                    emit(Result.Error(it.errorBody().toString()))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override suspend fun register(request: RequestRegister) {

    }
}