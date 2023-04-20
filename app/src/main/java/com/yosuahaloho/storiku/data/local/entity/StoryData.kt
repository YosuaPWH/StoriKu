package com.yosuahaloho.storiku.data.local.entity

import androidx.room.Entity
import com.yosuahaloho.storiku.utils.Constants.STORY_TABLE

/**
 * Created by Yosua on 20/04/2023
 */
@Entity(tableName = STORY_TABLE)
data class StoryData(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String
)
