package com.example.myunsplash.data.moshi_adapters.customs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoUrls(
    @Json(name = "regular")
    val regular: String,
)
