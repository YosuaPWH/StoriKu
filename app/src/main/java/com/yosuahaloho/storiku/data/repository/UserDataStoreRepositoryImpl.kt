package com.yosuahaloho.storiku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yosuahaloho.storiku.domain.model.User
import com.yosuahaloho.storiku.domain.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Yosua on 17/04/2023
 */
class UserDataStoreRepositoryImpl @Inject constructor(private val userDataStore: DataStore<Preferences>) :
    UserDataStoreRepository {

    private val name = stringPreferencesKey("name")
    private val email = stringPreferencesKey("email")
    private val id = stringPreferencesKey("id")
    private val isLogin = booleanPreferencesKey("islogin")

    override fun getDataUser() : Flow<User> {
        return userDataStore.data.map { pref ->
            User(
                name = pref[name] ?: "",
                email = pref[email] ?: "",
                userId = pref[id] ?: ""
            )
        }
    }

    override suspend fun loginUser(user: User) {
        userDataStore.edit { pref ->
            pref[name] = user.name
            pref[email] = user.email
            pref[id] = user.userId
            pref[isLogin] = true
        }
    }

    override suspend fun logout() {
        userDataStore.edit { pref ->
            pref[isLogin] = false
            pref[name] = ""
            pref[id] = ""
            pref[email] = ""
        }
    }
}