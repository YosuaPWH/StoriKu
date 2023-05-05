package com.yosuahaloho.storiku.utils

import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.domain.model.DetailStory

/**
 * Created by Yosua on 22/04/2023
 */
object DataMapper {

    fun DetailStory.detailStoryToEntity() = StoryData(
        id = this.id,
        name = this.name,
        description = this.description,
        photoUrl = this.photoUrl,
        createdAt = this.createdAt,
        lat = this.lat,
        lon = this.lon
    )

    fun StoryData.storyDataToModel() = DetailStory(
        id = this.id,
        name = this.name,
        description = this.description,
        photoUrl = this.photoUrl,
        createdAt = this.createdAt,
        lat = this.lat,
        lon = this.lon
    )
}