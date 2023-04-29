package com.yosuahaloho.storiku.data.remote

import com.yosuahaloho.storiku.data.remote.response.AddStoryResponse
import com.yosuahaloho.storiku.data.remote.response.allstories.AllStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Created by Yosua on 16/04/2023
 */
interface ApiStory {

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int
    ) : AllStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : Response<AddStoryResponse>
}