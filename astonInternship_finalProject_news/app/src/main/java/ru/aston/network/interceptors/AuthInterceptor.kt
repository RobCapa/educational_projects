package ru.aston.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val modifiedRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer cc67887a85c540ac877220f2e5af3239")
            .build()

        return chain.proceed(modifiedRequest)
    }
}