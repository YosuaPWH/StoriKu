package com.yosuahaloho.storiku.di

import com.yosuahaloho.storiku.BuildConfig
import com.yosuahaloho.storiku.data.remote.ApiAuth
import com.yosuahaloho.storiku.data.remote.ApiStory
import com.yosuahaloho.storiku.data.repository.AuthRepositoryImpl
import com.yosuahaloho.storiku.domain.AuthRepository
import com.yosuahaloho.storiku.utils.Constants.BASE_URL
import com.yosuahaloho.storiku.utils.Constants.TOKEN_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    private val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val authInterceptor = Interceptor { chain ->
        val requestHeaders = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $TOKEN_API")
            .build()

        chain.proceed(requestHeaders)
    }

    @Provides
    @Singleton
    fun provideApiAuthInstance() : ApiAuth {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiAuth::class.java)
    }

    @Provides
    @Singleton
    fun provideApiStoryInstance() : ApiStory {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiStory::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiAuth) : AuthRepository {
        return AuthRepositoryImpl(api)
    }
}