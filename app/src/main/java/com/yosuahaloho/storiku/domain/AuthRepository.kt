package com.yosuahaloho.storiku.domain

import com.yosuahaloho.storiku.data.model.RequestLogin
import com.yosuahaloho.storiku.data.model.RequestRegister
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yosua on 16/04/2023
 */
interface AuthRepository {
    fun login(request: RequestLogin) : Flow<Any>
    fun register(request: RequestRegister) : Flow<Any>
}