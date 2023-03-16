package com.example.myunsplash.data

import com.example.myunsplash.interfaces.Equatable
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoDetails(
    val location: String?,
    val tags: List<String>,
    val makeWith: String?,
    val model: String?,
    val exposure: String?,
    val aperture: String?,
    val focalLength: String?,
    val iso: String?,
    val description: String?,
    val downloads: String?,
    val photo: Photo,
) : Equatable
