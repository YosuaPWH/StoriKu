package com.yosuahaloho.storiku.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yosuahaloho.storiku.data.local.entity.StoryKeys

/**
 * Created by Yosua on 20/04/2023
 */
@Dao
interface StoryKeysDao {

    @Query("SELECT * FROM story_keys WHERE id = :id")
    suspend fun getStoryKeys(id: String): StoryKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStoryKeys(storyKeys: List<StoryKeys>)

    @Query("DELETE FROM story_keys")
    suspend fun deleteAllStoryKeys()
}