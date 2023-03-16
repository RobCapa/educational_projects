package com.example.myunsplash.data.moshi_adapters.customs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Exif(
    @Json(name = "make")
    val makeWith: String?,
    @Json(name = "model")
    val model: String?,
    @Json(name = "exposure_time")
    val exposure: String?,
    @Json(name = "aperture")
    val aperture: String?,
    @Json(name = "focal_length")
    val focalLength: String?,
    @Json(name = "iso")
    val iso: String?,
)