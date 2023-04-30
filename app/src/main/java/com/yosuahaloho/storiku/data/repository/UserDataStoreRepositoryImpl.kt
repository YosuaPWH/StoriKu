package com.yosuahaloho.storiku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yosuahaloho.storiku.domain.model.User
import com.yosuahaloho.storiku.domain.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Created by Yosua on 17/04/2023
 */
class UserDataStoreRepositoryImpl (private val userDataStore: DataStore<Preferences>) :
    UserDataStoreRepository {

    private val name = stringPreferencesKey("name")
    private val id = stringPreferencesKey("id")
    private val token = stringPreferencesKey("token")

    override suspend fun getDataUser(): Flow<User?> {
        return userDataStore.data.map { pref ->
            val name = pref[name]
            val id = pref[id]
            val token = pref[token]

            if (name.isNullOrEmpty() || id.isNullOrEmpty() || token.isNullOrEmpty()) {
                null
            } else {
                User(name = name, userId = id, token = token)
            }
        }
    }

    override suspend fun setDataUser(user: User) {
        userDataStore.edit { pref ->
            pref[name] = user.name
            pref[id] = user.userId
            pref[token] = user.token
        }
    }

    override suspend fun getToken(): String {
        return userDataStore.data.map { pref ->
            pref[token].orEmpty()
        }.first()
    }

    override suspend fun logout() : Boolean {
        return try {
            userDataStore.edit { pref ->
                pref.clear()
            }
            true
        } catch (e: Exception) {
            false
        }

    }


}