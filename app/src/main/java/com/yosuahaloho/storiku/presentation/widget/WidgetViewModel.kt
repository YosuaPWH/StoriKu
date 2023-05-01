package com.yosuahaloho.storiku.presentation.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Yosua on 01/05/2023
 */
@HiltViewModel
class WidgetViewModel @Inject constructor(private val repo: StoryRepository) : ViewModel() {

    fun getTenLatestStory() = repo.getTenLatestStory().asLiveData()
}