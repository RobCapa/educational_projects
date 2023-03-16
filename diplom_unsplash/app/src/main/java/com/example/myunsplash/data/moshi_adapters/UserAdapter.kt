package com.example.myunsplash.data.moshi_adapters

import com.example.myunsplash.data.User
import com.example.myunsplash.data.moshi_adapters.customs.CustomUser
import com.squareup.moshi.FromJson


class UserAdapter {
    @FromJson
    fun getUser(customUser: CustomUser): User {
        return with(customUser) {
            User(
                id,
                username,
                name,
                profileImage?.large,
                bio,
                location,
                email,
                totalPhotos,
                totalCollections,
                totalLikes,
                downloads,
            )
        }
    }
}