package com.example.myunsplash.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikeInfo(
    val likesNumber: Int,
    val likedByUser: Boolean,
)
