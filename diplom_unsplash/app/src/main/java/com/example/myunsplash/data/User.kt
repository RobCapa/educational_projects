package com.example.myunsplash.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val username: String,
    val name: String,
    val avatarUrl: String?,
    val bio: String?,
    val location: String?,
    val email: String?,
    val totalPhotos: String,
    val totalCollections: String,
    val totalLikes: String,
    val downloads: String,
)
