package com.example.data.network.api

import com.example.data.models.DProductsWrapper
import retrofit2.http.GET

interface StoreApi {

    @GET("v3/97e721a7-0a66-4cae-b445-83cc0bcf9010")
    suspend fun getAllProducts(): DProductsWrapper
}