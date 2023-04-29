package com.yosuahaloho.storiku.domain.repository

import com.yosuahaloho.storiku.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yosua on 17/04/2023
 */
interface UserDataStoreRepository {

    suspend fun setDataUser(user: User)
    suspend fun getDataUser() : Flow<User?>
    suspend fun logout()
    suspend fun getToken() : String

}