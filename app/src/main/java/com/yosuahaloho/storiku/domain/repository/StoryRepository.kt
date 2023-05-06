package com.yosuahaloho.storiku.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.data.remote.response.AddStoryResponse
import com.yosuahaloho.storiku.utils.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Yosua on 16/04/2023
 */
interface StoryRepository {

    fun getAllStories() : LiveData<PagingData<StoryData>>

    fun uploadStory(fileImage: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) : Flow<Result<AddStoryResponse?>>

    suspend fun deleteAllDataFromDatabase()

    fun getAllDataFromDatabase() : Flow<List<StoryData>>

}