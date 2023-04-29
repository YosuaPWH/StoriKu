package com.yosuahaloho.storiku.presentation.routing

import androidx.lifecycle.ViewModel
import com.yosuahaloho.storiku.domain.repository.UserDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Yosua on 27/04/2023
 */
@HiltViewModel
class RoutingViewModel @Inject constructor(private val dataStore: UserDataStoreRepository) : ViewModel() {

    suspend fun getUser() = dataStore.getDataUser()
}