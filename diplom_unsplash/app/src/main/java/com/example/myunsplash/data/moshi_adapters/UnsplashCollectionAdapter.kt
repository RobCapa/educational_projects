package com.example.myunsplash.data.moshi_adapters

import com.example.myunsplash.data.Author
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.data.moshi_adapters.customs.CustomCollection
import com.squareup.moshi.FromJson

class UnsplashCollectionAdapter {
    @FromJson
    fun getCollection(customCollection: CustomCollection): UnsplashCollection {
        val author = with(customCollection.customAuthor) {
            Author(
                id,
                name,
                username,
                avatarUrls.large,
            )
        }

        return with(customCollection) {
            UnsplashCollection(
                id,
                title,
                totalPhotos,
                coverPhoto.urls.regular,
                author
            )
        }
    }
}