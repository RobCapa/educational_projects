package com.example.myunsplash.data.moshi_adapters

import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.moshi_adapters.customs.CustomLikeInfo
import com.squareup.moshi.FromJson

class LikeInfoAdapter {
    @FromJson
    fun getLikeInfo(customLikeInfo: CustomLikeInfo): LikeInfo {
        return with(customLikeInfo) {
            LikeInfo(
                data.likesNumber,
                data.likedByUser,
            )
        }
    }
}
