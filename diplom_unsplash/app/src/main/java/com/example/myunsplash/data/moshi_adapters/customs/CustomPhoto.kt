package com.example.myunsplash.data.moshi_adapters.customs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomPhoto(
    @Json(name = "id")
    val id: String,
    @Json(name = "urls")
    val urls: PhotoUrls,
    @Json(name = "links")
    val links: PhotoLinks,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    @Json(name = "user")
    val customAuthor: CustomAuthor,
)
