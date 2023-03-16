package com.example.myunsplash.data

import com.example.myunsplash.interfaces.Equatable
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashCollection(
    val id: String,
    val title: String,
    val totalPhotos: String,
    val coverPhotoUrl: String,
    val author: Author,
) : Equatable
