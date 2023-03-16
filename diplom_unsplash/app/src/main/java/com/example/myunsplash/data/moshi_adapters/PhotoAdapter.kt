package com.example.myunsplash.data.moshi_adapters

import com.example.myunsplash.data.*
import com.example.myunsplash.data.moshi_adapters.customs.CustomPhoto
import com.squareup.moshi.FromJson

class PhotoAdapter {
    @FromJson
    fun getPhoto(customPhoto: CustomPhoto): Photo {
        val author = with(customPhoto.customAuthor) {
            Author(
                id,
                name,
                username,
                avatarUrls.large,
            )
        }

        return with(customPhoto) {
            Photo(
                id,
                urls.regular,
                links.download,
                likes,
                likedByUser,
                author
            )
        }
    }
}