package com.yosuahaloho.storiku.data.remote.response.login

import com.google.gson.annotations.SerializedName
import com.yosuahaloho.storiku.domain.model.User

/**
 * Created by Yosua on 16/04/2023
 */
data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("loginResult")
    val loginResult: User
)
