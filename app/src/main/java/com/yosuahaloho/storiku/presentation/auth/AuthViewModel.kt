package com.yosuahaloho.storiku.presentation.auth

import androidx.lifecycle.ViewModel
import com.yosuahaloho.storiku.domain.AuthRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Yosua on 16/04/2023
 */
@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepository) : ViewModel() {


}