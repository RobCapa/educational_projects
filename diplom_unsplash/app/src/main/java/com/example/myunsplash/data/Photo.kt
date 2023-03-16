package com.example.myunsplash.data

import com.example.myunsplash.interfaces.Equatable
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    val id: String,
    val url: String,
    val downloadUrl: String,
    var likes: Int,
    var likedByUser: Boolean,
    val author: Author,
) : Equatable
