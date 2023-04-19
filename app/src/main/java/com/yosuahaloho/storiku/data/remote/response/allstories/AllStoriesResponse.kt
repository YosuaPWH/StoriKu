package com.yosuahaloho.storiku.data.remote.response.allstories

import com.yosuahaloho.storiku.data.remote.response.DetailStory

/**
 * Created by Yosua on 19/04/2023
 */
data class AllStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<DetailStory>
)
