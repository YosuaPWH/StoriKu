package com.yosuahaloho.storiku.utils

import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.data.remote.response.DetailStory

/**
 * Created by Yosua on 22/04/2023
 */
object DataMapper {

    fun DetailStory.detailStoryToEntity() = StoryData(
        id = this.id,
        name = this.name,
        description = this.description,
        photoUrl = this.photoUrl
    )
}