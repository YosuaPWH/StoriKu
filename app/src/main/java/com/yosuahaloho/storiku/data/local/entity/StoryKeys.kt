package com.yosuahaloho.storiku.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yosuahaloho.storiku.utils.Constants.STORY_KEYS

/**
 * Created by Yosua on 20/04/2023
 */
@Entity(tableName = STORY_KEYS)
data class StoryKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)