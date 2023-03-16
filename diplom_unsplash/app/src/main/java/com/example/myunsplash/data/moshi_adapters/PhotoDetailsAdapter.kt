package com.example.myunsplash.data.moshi_adapters

import com.example.myunsplash.data.Author
import com.example.myunsplash.data.Photo
import com.example.myunsplash.data.PhotoDetails
import com.example.myunsplash.data.moshi_adapters.customs.CustomPhotoWithDetails
import com.squareup.moshi.FromJson

class PhotoDetailsAdapter {
    @FromJson
    fun photoDetails(customPhotoAuthor: CustomPhotoWithDetails): PhotoDetails {
        val author = with(customPhotoAuthor.customAuthor) {
            Author(
                id,
                name,
                username,
                avatarUrls.large,
            )
        }

        val photo = with(customPhotoAuthor) {
            Photo(
                id,
                urls.regular,
                links.download,
                likes,
                likedByUser,
                author
            )
        }

        return with(customPhotoAuthor) {
            PhotoDetails(
                listOfNotNull(location.city, location.country)
                    .joinToString()
                    .takeIf { it.isNotBlank() },
                tags.map { it.name },
                exif.makeWith,
                exif.model,
                exif.exposure,
                exif.aperture,
                exif.focalLength,
                exif.iso,
                description,
                downloads,
                photo,
            )
        }
    }
}