package com.example.myunsplash.data.moshi_adapters.customs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileImage(
    @Json(name = "large")
    val large: String
)
