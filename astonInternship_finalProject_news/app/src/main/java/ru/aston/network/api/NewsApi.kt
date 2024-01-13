package ru.aston.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.aston.data.NewsSourceWrapper
import ru.aston.data.NewsWrapper

interface NewsApi {

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("language") language: String? = null,
        @Query("sortBy") sortBy: String? = null,
    ): NewsSourceWrapper

    @GET("everything")
    suspend fun getNews(
        @Query("q") searchCriteria: String = "*",
        @Query("language") language: String? = null,
        @Query("sources") sources: String? = null,
        @Query("from") fromDate: String? = null,
        @Query("to") toDate: String? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("page") page: Int? = null,
        @Query("sortBy") sortBy: String? = null,
    ): NewsWrapper

    @GET("top-headlines")
    suspend fun getNewsByCategory(
        @Query("q") searchCriteria: String? = null,
        @Query("category") category: String? = null,
        @Query("language") language: String? = null,
        @Query("sources") sources: String? = null,
        @Query("from") fromDate: String? = null,
        @Query("to") toDate: String? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("page") page: Int? = null,
        @Query("sortBy") sortBy: String? = null,
    ): NewsWrapper
}