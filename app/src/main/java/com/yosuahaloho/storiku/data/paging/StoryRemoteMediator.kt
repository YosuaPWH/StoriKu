package com.yosuahaloho.storiku.data.paging

import android.provider.MediaStore.Audio.Media
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yosuahaloho.storiku.data.local.db.StoryDatabase
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.data.local.entity.StoryKeys
import com.yosuahaloho.storiku.data.remote.ApiStory
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import com.yosuahaloho.storiku.utils.DataMapper.detailStoryToEntity
import com.yosuahaloho.storiku.utils.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Yosua on 21/04/2023
 */
@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(
    private val api: ApiStory,
    private val db: StoryDatabase
) : RemoteMediator<Int, StoryData>() {

    private val storyDataDao = db.storyDataDao()
    private val storyKeysDao = db.storyKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryData>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.APPEND -> {
                    val remoteKeys = getStoryKeysForTheLastStory(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }

                LoadType.REFRESH -> {
                    val remoteKeys = getStoryKeysCloseToCurrentPositionStory(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getStoryKeysForTheFirstStory(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }
            }

            val response = api.getAllStories(currentPage).listStory.map { it.detailStoryToEntity() }
            val endOfStoryReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfStoryReached) null else currentPage + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDataDao.deleteAllStory()
                    storyKeysDao.deleteAllStoryKeys()
                }
                val keys = response.map { storyData ->
                    StoryKeys(
                        id = storyData.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                storyKeysDao.addStoryKeys(keys)
                storyDataDao.addStory(response)
            }
            MediatorResult.Success(endOfPaginationReached = endOfStoryReached)

            MediatorResult.Error(IllegalArgumentException())

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getStoryKeysCloseToCurrentPositionStory(
        state: PagingState<Int, StoryData>
    ): StoryKeys? {
        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.id?.let { id ->
                storyKeysDao.getStoryKeys(id)
            }
        }
    }

    private suspend fun getStoryKeysForTheFirstStory(
        state: PagingState<Int, StoryData>
    ): StoryKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            storyKeysDao.getStoryKeys(it.id)
        }
    }

    private suspend fun getStoryKeysForTheLastStory(
        state: PagingState<Int, StoryData>
    ): StoryKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            storyKeysDao.getStoryKeys(it.id)
        }
    }

}