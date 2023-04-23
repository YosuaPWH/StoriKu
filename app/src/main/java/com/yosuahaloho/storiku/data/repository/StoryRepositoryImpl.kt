package com.yosuahaloho.storiku.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yosuahaloho.storiku.data.local.db.StoryDatabase
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.data.paging.StoryRemoteMediator
import com.yosuahaloho.storiku.data.remote.ApiStory
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import com.yosuahaloho.storiku.utils.Constants.ITEM_PER_PAGE
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yosua on 20/04/2023
 */
class StoryRepositoryImpl(
    private val api: ApiStory,
    private val db: StoryDatabase
) : StoryRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllStories(): Flow<PagingData<StoryData>> {
        val pagingSource = { db.storyDataDao().getAllStory() }
        return Pager(
            config = PagingConfig(pageSize = ITEM_PER_PAGE),
            remoteMediator = StoryRemoteMediator(
                api = api,
                db = db
            ),
            pagingSourceFactory = pagingSource
        ).flow
    }
}