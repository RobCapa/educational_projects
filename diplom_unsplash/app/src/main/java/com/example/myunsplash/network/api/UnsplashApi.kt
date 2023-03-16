package com.example.myunsplash.network.api

import com.example.myunsplash.data.*
import com.example.myunsplash.data.UnsplashCollection
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UnsplashApi {
    /** order_by values: latest (default), oldest, popular */
    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
        @Query("order_by") order_by: String?,
    ): List<Photo>

    @GET("/users/{username}/likes")
    suspend fun getFavoritesPhotos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
        @Query("order_by") order_by: String?,
    ): List<Photo>

    @GET("/users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
        @Query("order_by") order_by: String?,
    ): List<Photo>

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
    ): ResponseBody

    @GET("/search/collections")
    suspend fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
    ): ResponseBody

    @GET("/search/users")
    suspend fun searchUsers(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
    ): ResponseBody

    @GET("/collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
    ): List<Photo>

    @GET("/users/{username}/collections")
    suspend fun getUserCollections(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
    ): List<UnsplashCollection>

    @GET("/collections")
    suspend fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int?,
    ): List<UnsplashCollection>

    @GET("/collections/{id}")
    suspend fun getCollection(
        @Path("id") id: String,
    ): UnsplashCollection

    @GET("/photos/{id}")
    suspend fun getPhotoDetails(
        @Path("id") photoId: String,
    ): PhotoDetails

    @GET("/me")
    suspend fun getCurrentUser(
    ): User

    @GET("/users/{username}")
    suspend fun getUser(
        @Path("username") username: String,
    ): User

    @POST("/photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") photoId: String,
    ): LikeInfo

    @DELETE("/photos/{id}/like")
    suspend fun unLikePhoto(
        @Path("id") photoId: String,
    ): LikeInfo
}