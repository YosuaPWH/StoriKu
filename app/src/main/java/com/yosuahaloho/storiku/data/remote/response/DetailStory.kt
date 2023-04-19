package com.yosuahaloho.storiku.data.remote.response

data class DetailStory (
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double,
    val lon: Double
)
