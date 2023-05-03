package com.yosuahaloho.storiku.presentation.list_story

import androidx.lifecycle.ViewModel
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Yosua on 20/04/2023
 */
@HiltViewModel
class ListStoryViewModel @Inject constructor(private val repo: StoryRepository) : ViewModel() {

    fun getAllStories() = repo.getAllStories()
    suspend fun deleteAllDataFromDatabase() = repo.deleteAllDataFromDatabase()

}