package com.yosuahaloho.storiku.di

import android.content.Context
import androidx.room.Room
import com.yosuahaloho.storiku.data.local.db.StoryDatabase
import com.yosuahaloho.storiku.utils.Constants.STORY_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Yosua on 20/04/2023
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseStory(
        @ApplicationContext context: Context
    ): StoryDatabase {
        return Room.databaseBuilder(
            context,
            StoryDatabase::class.java,
            STORY_DATABASE
        ).build()
    }

}