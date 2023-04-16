package com.yosuahaloho.storiku.domain.model

/**
 * Created by Yosua on 16/04/2023
 */
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
