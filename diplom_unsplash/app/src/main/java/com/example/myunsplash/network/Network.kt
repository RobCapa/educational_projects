package com.example.myunsplash.network

import com.example.myunsplash.data.moshi_adapters.*
import com.example.myunsplash.network.api.UnsplashApi
import com.example.myunsplash.network.interceptors.UnsplashInterceptor
import com.example.myunsplash.di.interfaces.GetApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject

class Network @Inject constructor(
    unsplashInterceptor: UnsplashInterceptor,
) : GetApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.unsplash.com")
        .client(
            OkHttpClient().newBuilder()
                .addNetworkInterceptor(unsplashInterceptor)
                .build()
        )
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(PhotoAdapter())
                    .add(PhotoDetailsAdapter())
                    .add(UnsplashCollectionAdapter())
                    .add(LikeInfoAdapter())
                    .add(UserAdapter())
                    .build()
            )
        )
        .build()

    override fun getUnsplashApi(): UnsplashApi {
        return retrofit.create()
    }
}