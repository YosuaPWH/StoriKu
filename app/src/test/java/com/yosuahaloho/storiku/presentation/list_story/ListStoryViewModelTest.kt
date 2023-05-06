package com.yosuahaloho.storiku.presentation.list_story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import com.yosuahaloho.storiku.utils.Dummy
import com.yosuahaloho.storiku.utils.MainDispatcherRule
import com.yosuahaloho.storiku.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Yosua on 06/05/2023
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var listStoryViewModel: ListStoryViewModel
    private val dummyStory = Dummy.generateDummyStoryData()

    @Before
    fun setup() {
        listStoryViewModel = ListStoryViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest{
        val data: PagingData<StoryData> = StoryPagingSource.snapshot(dummyStory)
        val expectedData = MutableLiveData<PagingData<StoryData>>()
        expectedData.value = data
        Mockito.`when`(storyRepository.getAllStories()).thenReturn(expectedData)

        val actualStory: PagingData<StoryData> = listStoryViewModel.getAllStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.COMPARATOR,
            updateCallback = storyListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)


        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryData> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryData>>()
        expectedStory.value = data

        Mockito.`when`(storyRepository.getAllStories()).thenReturn(expectedStory)

        val actualStory: PagingData<StoryData> = listStoryViewModel.getAllStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.COMPARATOR,
            updateCallback = storyListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }

}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryData>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryData>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryData>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<StoryData>): PagingData<StoryData> {
            return PagingData.from(items)
        }
    }
}

val storyListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {

    }

    override fun onRemoved(position: Int, count: Int) {
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
    }
}