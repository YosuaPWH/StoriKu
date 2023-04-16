package com.yosuahaloho.storiku.utils

/**
 * Created by Yosua on 16/04/2023
 */
sealed class Result<out T> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}