package com.yosuahaloho.storiku.di

import com.yosuahaloho.storiku.BuildConfig
import com.yosuahaloho.storiku.data.local.db.StoryDatabase
import com.yosuahaloho.storiku.data.remote.ApiAuth
import com.yosuahaloho.storiku.data.remote.ApiStory
import com.yosuahaloho.storiku.data.repository.AuthRepositoryImpl
import com.yosuahaloho.storiku.data.repository.StoryRepositoryImpl
import com.yosuahaloho.storiku.domain.repository.AuthRepository
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import com.yosuahaloho.storiku.domain.repository.UserDataStoreRepository
import com.yosuahaloho.storiku.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Yosua on 16/04/2023
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(dataStore: UserDataStoreRepository) = Interceptor { chain ->
        var token: String
        runBlocking {
            withContext(Dispatchers.Default) {
                token = dataStore.getToken()
            }
        }
        val requestHeaders = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        chain.proceed(requestHeaders)
    }

    @Provides
    @Singleton
    fun provideApiAuthInstance(loggingInterceptor: HttpLoggingInterceptor) : ApiAuth =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .build())
            .build()
            .create(ApiAuth::class.java)

    @Provides
    @Singleton
    fun provideApiStoryInstance(loggingInterceptor: HttpLoggingInterceptor, authInterceptor: Interceptor) : ApiStory =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .build())
            .build()
            .create(ApiStory::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiAuth, dataStore: UserDataStoreRepository) : AuthRepository {
        return AuthRepositoryImpl(api, dataStore)
    }

    @Provides
    @Singleton
    fun provideStoryRepository(api: ApiStory, db: StoryDatabase) : StoryRepository {
        return StoryRepositoryImpl(api, db)
    }

}