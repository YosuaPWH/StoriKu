package com.yosuahaloho.storiku.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yosuahaloho.storiku.utils.Constants.STORY_TABLE

/**
 * Created by Yosua on 20/04/2023
 */
@Entity(tableName = STORY_TABLE)
data class StoryData(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double? = null,
    val lon: Double? = null
)
