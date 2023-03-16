package com.example.myunsplash.data.moshi_adapters.customs

import com.example.myunsplash.data.moshi_adapters.PhotoDetailsAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomPhotoWithDetails(
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
    @Json(name = "exif")
    val exif: Exif,
    @Json(name = "location")
    val location: Location,
    @Json(name = "tags")
    val tags: List<Tag>,
    @Json(name = "description")
    val description: String?,
    @Json(name = "downloads")
    val downloads: String,
)
