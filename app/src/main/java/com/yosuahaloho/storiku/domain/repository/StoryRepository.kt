package com.yosuahaloho.storiku.domain.repository

import com.yosuahaloho.storiku.data.remote.response.allstories.AllStoriesResponse
import com.yosuahaloho.storiku.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yosua on 16/04/2023
 */
interface StoryRepository {

    fun getAllStories() : Flow<Result<AllStoriesResponse?>>
}