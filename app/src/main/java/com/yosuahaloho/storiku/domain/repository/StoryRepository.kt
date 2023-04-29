package com.yosuahaloho.storiku.domain.repository

import androidx.paging.PagingData
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.data.remote.response.AddStoryResponse
import com.yosuahaloho.storiku.data.remote.response.allstories.AllStoriesResponse
import com.yosuahaloho.storiku.utils.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Yosua on 16/04/2023
 */
interface StoryRepository {

    fun getAllStories() : Flow<PagingData<StoryData>>

    fun uploadStory(fileImage: MultipartBody.Part, description: RequestBody) : Flow<Result<AddStoryResponse?>>

    fun deleteAllStories()
}