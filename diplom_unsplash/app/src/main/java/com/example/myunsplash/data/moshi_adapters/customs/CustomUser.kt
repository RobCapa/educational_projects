package com.example.myunsplash.data.moshi_adapters.customs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomUser(
    @Json(name = "id")
    val id: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "profile_image")
    val profileImage: ProfileImage?,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "location")
    val location: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "total_photos")
    val totalPhotos: String,
    @Json(name = "total_collections")
    val totalCollections: String,
    @Json(name = "total_likes")
    val totalLikes: String,
    @Json(name = "downloads")
    val downloads: String,
)
