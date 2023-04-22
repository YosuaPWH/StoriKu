package com.yosuahaloho.storiku.data.remote

import com.yosuahaloho.storiku.data.remote.response.allstories.AllStoriesResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Yosua on 16/04/2023
 */
interface ApiStory {

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int
    ) : AllStoriesResponse
}