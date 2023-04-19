package com.yosuahaloho.storiku.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.yosuahaloho.storiku.data.repository.UserDataStoreRepositoryImpl
import com.yosuahaloho.storiku.domain.repository.UserDataStoreRepository
import com.yosuahaloho.storiku.utils.Constants.DATA_USER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Created by Yosua on 17/04/2023
 */


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesUserDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, DATA_USER)),
            produceFile = { context.preferencesDataStoreFile(DATA_USER) },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Provides
    @Singleton
    fun getDataStore(userDataStore: DataStore<Preferences>): UserDataStoreRepository {
        return UserDataStoreRepositoryImpl(userDataStore)
    }
}