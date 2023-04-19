package com.yosuahaloho.storiku.data.repository

import com.google.gson.Gson
import com.yosuahaloho.storiku.data.remote.ApiStory
import com.yosuahaloho.storiku.data.remote.response.allstories.AllStoriesResponse
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import com.yosuahaloho.storiku.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Yosua on 20/04/2023
 */
class StoryRepositoryImpl @Inject constructor(private val api: ApiStory) : StoryRepository {

    override fun getAllStories(): Flow<Result<AllStoriesResponse?>> = flow {
        emit(Result.Loading)
        try {
            api.getAllStories().let {
                if (it.isSuccessful) {
                    val body = it.body()
                    emit(Result.Success(body))
                } else {
                    val errorMessage = Gson().fromJson(
                        it.errorBody()?.charStream(),
                        AllStoriesResponse::class.java
                    )
                    emit(Result.Error(errorMessage.message))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}