package com.yosuahaloho.storiku.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yosuahaloho.storiku.data.local.dao.StoryDao
import com.yosuahaloho.storiku.data.local.dao.StoryKeysDao
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.data.local.entity.StoryKeys

/**
 * Created by Yosua on 20/04/2023
 */
@Database(entities = [StoryData::class, StoryKeys::class], version = 2)
abstract class StoryDatabase: RoomDatabase() {

    abstract fun storyDataDao(): StoryDao
    abstract fun storyKeysDao(): StoryKeysDao
}