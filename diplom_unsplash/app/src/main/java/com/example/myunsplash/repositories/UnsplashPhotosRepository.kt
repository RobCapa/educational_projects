package com.example.myunsplash.repositories

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.*
import com.example.myunsplash.R
import com.example.myunsplash.UnsplashApp
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.Photo
import com.example.myunsplash.data.PhotoDetails
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.data.moshi_adapters.PhotoAdapter
import com.example.myunsplash.data.moshi_adapters.UnsplashCollectionAdapter
import com.example.myunsplash.data.moshi_adapters.customs.CustomCollection
import com.example.myunsplash.data.moshi_adapters.customs.CustomPhoto
import com.example.myunsplash.exceptions.WorkAlreadyExistsException
import com.example.myunsplash.di.interfaces.GetApi
import com.example.myunsplash.di.interfaces.PhotosRepository
import com.example.myunsplash.repositories.managers.DownloadPhotoManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject

class UnsplashPhotosRepository @Inject constructor(
    private val network: GetApi,
) : PhotosRepository {
    private val moshi = Moshi.Builder().build()

    override suspend fun getPhotosFlow(
        pagingConfig: PagingConfig
    ): Flow<PagingData<Photo>> {
        return getPagingDataFlow(pagingConfig) { page, counts ->
            network.getUnsplashApi().getPhotos(
                page,
                counts,
                null,
            )
        }
    }

    override suspend fun getFavoritesPhotosFlow(
        username: String,
        pagingConfig: PagingConfig,
    ): Flow<PagingData<Photo>> {
        return getPagingDataFlow(pagingConfig) { page, counts ->
            network.getUnsplashApi().getFavoritesPhotos(
                username,
                page,
                counts,
                null,
            )
        }
    }

    override suspend fun getUserPhotosFlow(
        username: String,
        pagingConfig: PagingConfig,
    ): Flow<PagingData<Photo>> {
        return getPagingDataFlow(pagingConfig) { page, counts ->
            network.getUnsplashApi().getUserPhotos(
                username,
                page,
                counts,
                null,
            )
        }
    }

    override suspend fun getUserCollectionsFlow(
        username: String,
        pagingConfig: PagingConfig,
    ): Flow<PagingData<UnsplashCollection>> {
        return getPagingDataFlow(pagingConfig) { page, counts ->
            network.getUnsplashApi().getUserCollections(
                username,
                page,
                counts,
            )
        }
    }

    override suspend fun getCollectionPhotosFlow(
        collectionId: String,
        pagingConfig: PagingConfig,
    ): Flow<PagingData<Photo>> {
        return getPagingDataFlow(pagingConfig) { page, counts ->
            network.getUnsplashApi().getCollectionPhotos(
                collectionId,
                page,
                counts,
            )
        }
    }

    override suspend fun getCollection(
        collectionId: String,
    ): UnsplashCollection {
        return network.getUnsplashApi().getCollection(collectionId)
    }

    override suspend fun getCollectionsFlow(
        pagingConfig: PagingConfig,
    ): Flow<PagingData<UnsplashCollection>> {
        return getPagingDataFlow(pagingConfig) { page, counts ->
            network.getUnsplashApi().getCollections(
                page,
                counts,
            )
        }
    }

    override suspend fun searchPhotosFlow(
        query: String,
        pagingConfig: PagingConfig,
    ): Flow<PagingData<Photo>> {
        return getPagingDataFlow(pagingConfig) { page, counts ->
            network.getUnsplashApi().searchPhotos(
                query,
                page,
                counts,
            ).let(::parseResultOfSearchPhotos)
        }
    }

    override suspend fun searchCollectionsFlow(
        query: String,
        pagingConfig: PagingConfig,
    ): Flow<PagingData<UnsplashCollection>> {
        return getPagingDataFlow(pagingConfig) { page, counts ->
            network.getUnsplashApi().searchCollections(
                query,
                page,
                counts,
            ).let(::parseResultOfSearchCollections)
        }
    }

    private fun parseResultOfSearchPhotos(responseBody: ResponseBody): List<Photo> {
        val adapter = PhotoAdapter()
        val type = Types.newParameterizedType(List::class.java, CustomPhoto::class.java)
        return moshi
            .adapter<List<CustomPhoto>>(type)
            .nonNull()
            .fromJson(getResultOfSearchFromJson(responseBody))
            ?.map { adapter.getPhoto(it) } ?: emptyList()
    }

    private fun parseResultOfSearchCollections(responseBody: ResponseBody): List<UnsplashCollection> {
        val adapter = UnsplashCollectionAdapter()
        val type = Types.newParameterizedType(List::class.java, CustomCollection::class.java)
        return moshi
            .adapter<List<CustomCollection>>(type)
            .nonNull()
            .fromJson(getResultOfSearchFromJson(responseBody))
            ?.map { adapter.getCollection(it) } ?: emptyList()
    }

    private fun getResultOfSearchFromJson(responseBody: ResponseBody): String {
        return JSONObject(responseBody.string())
            .getJSONArray("results")
            .toString()
    }

    private fun <T : Any> getPagingDataFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(),
        getItems: suspend (Int, Int) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            pagingConfig,
            pagingSourceFactory = {
                Source(getItems)
            }
        ).flow
    }

    override suspend fun getPhotoDetails(photoId: String): PhotoDetails {
        return withContext(Dispatchers.IO) {
            network.getUnsplashApi().getPhotoDetails(photoId)
        }
    }

    override suspend fun likePhoto(photoId: String): LikeInfo {
        return withContext(Dispatchers.IO) {
            network.getUnsplashApi().likePhoto(photoId)
        }
    }

    override suspend fun unlikePhoto(photoId: String): LikeInfo {
        return withContext(Dispatchers.IO) {
            network.getUnsplashApi().unLikePhoto(photoId)
        }
    }

    override suspend fun downloadPhoto(url: String): LiveData<WorkInfo> {
        return withContext(Dispatchers.IO) {
            if (checkUniqueWorkExists(url)) throw WorkAlreadyExistsException()

            val workData = workDataOf(
                DownloadPhotoManager.KEY_URL to url,
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            OneTimeWorkRequestBuilder<DownloadPhotoManager>()
                .setInputData(workData)
                .setConstraints(constraints)
                .build()
                .let { workRequest ->
                    with(WorkManager.getInstance(UnsplashApp.getAppContext())) {
                        enqueueUniqueWork(url, ExistingWorkPolicy.KEEP, workRequest)
                        return@withContext getWorkInfosForUniqueWorkLiveData(url).map { it[0] }
                    }
                }
        }
    }

    override fun shareImageUrl(photoId: String, context: Context) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.repository_shareUrl).format(photoId)
            )
            type = "text/plain"
        }
        context.startActivity(
            Intent.createChooser(
                sendIntent,
                context.getString(R.string.repository_shareTitle)
            )
        )
    }

    override fun openPhotoIn(uri: Uri, context: Context) {
        val intent = getSendPhotoIntent(uri, context)
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.repository_openPhotoIn)
            )
        )
    }

    override fun getSendPhotoIntent(uri: Uri, context: Context): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_STREAM,
                uri
            )
            type = "image/jpeg"
        }
    }

    private suspend fun checkUniqueWorkExists(workName: String): Boolean {
        val workManager = WorkManager.getInstance(UnsplashApp.getAppContext())
        val workInfos = workManager.getWorkInfosForUniqueWork(workName).await()
        if (workInfos.size == 1) {
            val workInfo = workInfos[0]
            if (workInfos[0].state == WorkInfo.State.BLOCKED
                || workInfo.state == WorkInfo.State.ENQUEUED
                || workInfo.state == WorkInfo.State.RUNNING
            ) return true
        }
        return false
    }

    override fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 30, enablePlaceholders = false)
    }
}