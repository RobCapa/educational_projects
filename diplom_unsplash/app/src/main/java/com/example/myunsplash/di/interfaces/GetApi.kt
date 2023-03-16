package com.example.myunsplash.di.interfaces

import com.example.myunsplash.network.api.UnsplashApi

interface GetApi {
    fun getUnsplashApi() : UnsplashApi
}