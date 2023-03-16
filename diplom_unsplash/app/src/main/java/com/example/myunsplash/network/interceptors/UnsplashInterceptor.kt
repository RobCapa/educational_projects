package com.example.myunsplash.network.interceptors

import com.example.myunsplash.di.interfaces.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UnsplashInterceptor @Inject constructor(
    authRepository: AuthRepository,
)  : Interceptor {
    private val accessToken = authRepository.getAccessToken()

    override fun intercept(chain: Interceptor.Chain): Response {
        val modifiedRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(modifiedRequest)
    }
}