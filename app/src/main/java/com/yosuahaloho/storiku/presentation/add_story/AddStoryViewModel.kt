package com.yosuahaloho.storiku.presentation.add_story

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yosuahaloho.storiku.domain.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * Created by Yosua on 28/04/2023
 */
@HiltViewModel
class AddStoryViewModel @Inject constructor(private val repo: StoryRepository) : ViewModel() {

    val currentLocationLatitude = MutableLiveData(0.0)
    val currentLocationLongitude = MutableLiveData(0.0)

    fun uploadStory(
        fileImage: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = repo.uploadStory(fileImage, description, lat, lon)

    fun makeLocationEmpty() {
        currentLocationLatitude.value = 0.0
        currentLocationLongitude.value = 0.0
    }
}