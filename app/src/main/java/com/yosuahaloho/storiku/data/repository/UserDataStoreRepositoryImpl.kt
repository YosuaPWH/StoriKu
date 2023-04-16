package com.yosuahaloho.storiku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yosuahaloho.storiku.domain.UserDataStoreRepository
import javax.inject.Inject

/**
 * Created by Yosua on 17/04/2023
 */
class UserDataStoreRepositoryImpl @Inject constructor(private val userDataStore: DataStore<Preferences>) :
    UserDataStoreRepository {

    override fun setDataUser() {

    }

    override fun getDataUser() {

    }
}