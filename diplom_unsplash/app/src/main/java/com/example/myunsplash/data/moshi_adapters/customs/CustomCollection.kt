package com.example.myunsplash.data.moshi_adapters.customs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomCollection(
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "total_photos")
    val totalPhotos: String,
    @Json(name = "cover_photo")
    val coverPhoto: CoverPhoto,
    @Json(name = "user")
    val customAuthor: CustomAuthor,
)