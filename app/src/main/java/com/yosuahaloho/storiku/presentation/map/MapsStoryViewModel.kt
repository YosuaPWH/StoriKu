package com.yosuahaloho.storiku.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import com.yosuahaloho.storiku.domain.model.DetailStory
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import com.yosuahaloho.storiku.utils.DataMapper.storyDataToModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Yosua on 05/05/2023
 */
@HiltViewModel
class MapsStoryViewModel @Inject constructor(private val repo: StoryRepository): ViewModel() {

    fun getStoryFromDatabase() = repo.getAllDataFromDatabase().asLiveData().distinctUntilChanged()
}