package com.example.myunsplash.di.interfaces

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.WorkInfo
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.Photo
import com.example.myunsplash.data.PhotoDetails
import com.example.myunsplash.data.UnsplashCollection
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    suspend fun getPhotosFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<Photo>>

    suspend fun getFavoritesPhotosFlow(
        username: String,
        pagingConfig: PagingConfig = getDefaultPageConfig(),
    ): Flow<PagingData<Photo>>

    suspend fun getUserPhotosFlow(
        username: String,
        pagingConfig: PagingConfig = getDefaultPageConfig(),
    ): Flow<PagingData<Photo>>

    suspend fun getUserCollectionsFlow(
        username: String,
        pagingConfig: PagingConfig = getDefaultPageConfig(),
    ): Flow<PagingData<UnsplashCollection>>

    suspend fun getCollectionPhotosFlow(
        collectionId: String,
        pagingConfig: PagingConfig = getDefaultPageConfig(),
    ): Flow<PagingData<Photo>>

    suspend fun getCollection(
        collectionId: String,
    ): UnsplashCollection

    suspend fun getCollectionsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(),
    ): Flow<PagingData<UnsplashCollection>>

    suspend fun searchPhotosFlow(
        query: String,
        pagingConfig: PagingConfig = getDefaultPageConfig(),
    ): Flow<PagingData<Photo>>

    suspend fun searchCollectionsFlow(
        query: String,
        pagingConfig: PagingConfig = getDefaultPageConfig(),
    ): Flow<PagingData<UnsplashCollection>>

    suspend fun getPhotoDetails(photoId: String): PhotoDetails

    suspend fun likePhoto(photoId: String): LikeInfo

    suspend fun unlikePhoto(photoId: String): LikeInfo

    suspend fun downloadPhoto(url: String): LiveData<WorkInfo>

    fun shareImageUrl(photoId: String, context: Context)

    fun openPhotoIn(uri: Uri, context: Context)

    fun getSendPhotoIntent(uri: Uri, context: Context): Intent

    fun getDefaultPageConfig(): PagingConfig
}