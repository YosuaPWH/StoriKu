package com.yosuahaloho.storiku.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yosuahaloho.storiku.data.local.db.StoryDatabase
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.data.paging.StoryRemoteMediator
import com.yosuahaloho.storiku.data.remote.ApiStory
import com.yosuahaloho.storiku.data.remote.response.AddStoryResponse
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import com.yosuahaloho.storiku.utils.Constants.ITEM_PER_PAGE
import com.yosuahaloho.storiku.utils.Result
import com.yosuahaloho.storiku.utils.getError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Yosua on 20/04/2023
 */
class StoryRepositoryImpl(
    private val api: ApiStory,
    private val db: StoryDatabase
) : StoryRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllStories(): Flow<PagingData<StoryData>> {
        val pagingSource = { db.storyDataDao().getAllStory() }
        return Pager(
            config = PagingConfig(pageSize = ITEM_PER_PAGE),
            remoteMediator = StoryRemoteMediator(
                api = api,
                db = db
            ),
            pagingSourceFactory = pagingSource
        ).flow
    }

    override fun uploadStory(fileImage: MultipartBody.Part, description: RequestBody) = flow {
        emit(Result.Loading)
        try {
            api.uploadStory(file = fileImage, description = description).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    emit(Result.Success(body))
                } else {
                    val errorMessage = it.getError<AddStoryResponse>().message
                    emit(Result.Error(errorMessage))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun deleteAllDataFromDatabase() {
        db.storyDataDao().deleteAllStory()
        db.storyKeysDao().deleteAllStoryKeys()
    }

    override fun getAllDataFromDatabase(): Flow<List<StoryData>> = flow {
        try {
            val data = db.storyDataDao().getAllDataFromDatabaseWhereLocationNotNull()
            emit(data)
        } catch (e: Exception) {
            Log.e("StoryRepositoryImpl", "Get All Data From Database Error: $e")
        }
    }.flowOn(Dispatchers.IO)
}