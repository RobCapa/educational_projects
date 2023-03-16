package com.example.myunsplash.data.moshi_adapters.customs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomLikeInfo(
    @Json(name = "photo")
    val data: CustomPhotoLikeInfo
)

@JsonClass(generateAdapter = true)
data class CustomPhotoLikeInfo(
    @Json(name = "likes")
    val likesNumber: Int,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
)
