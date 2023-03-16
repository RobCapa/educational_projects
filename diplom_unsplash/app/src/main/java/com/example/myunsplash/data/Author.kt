package com.example.myunsplash.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Author(
    val id: String,
    val name: String,
    val username: String,
    val avatarUrl: String,
)
