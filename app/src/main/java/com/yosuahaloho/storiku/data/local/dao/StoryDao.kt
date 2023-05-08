package com.yosuahaloho.storiku.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yosuahaloho.storiku.data.local.entity.StoryData

/**
 * Created by Yosua on 20/04/2023
 */
@Dao
interface StoryDao {

    @Query("SELECT * FROM story_table")
    fun getAllStory(): PagingSource<Int, StoryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStory(story: List<StoryData>)

    @Query("DELETE FROM story_table")
    suspend fun deleteAllStory()

    @Query("SELECT * FROM story_table LIMIT 10")
    fun getTenLatestStory(): List<StoryData>

}