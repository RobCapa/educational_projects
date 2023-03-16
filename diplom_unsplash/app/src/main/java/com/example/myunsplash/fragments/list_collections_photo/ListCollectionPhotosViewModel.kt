package com.example.myunsplash.fragments.list_collections_photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.Photo
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.di.interfaces.PhotosRepository
import com.example.myunsplash.utils.ViewModelWithHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCollectionPhotosViewModel @Inject constructor(
    private val photosRep: PhotosRepository
) : ViewModelWithHandler() {
    private val _unsplashCollectionLiveData = MutableLiveData<UnsplashCollection>()
    val unsplashCollectionLiveData: LiveData<UnsplashCollection>
        get() = _unsplashCollectionLiveData

    private val _collectionPhotosLiveDataFlow = MutableLiveData<Flow<PagingData<Photo>>>()
    val collectionPhotosLiveDataFlow: LiveData<Flow<PagingData<Photo>>>
        get() = _collectionPhotosLiveDataFlow

    private val _likeInfoLiveData = MutableLiveData<Pair<LikeInfo, Int>>()
    val likeInfoLiveData: LiveData<Pair<LikeInfo, Int>>
        get() = _likeInfoLiveData

    fun getCollection(collectionId: String) {
        viewModelScope.launch(handler) {
            _unsplashCollectionLiveData.postValue(
                photosRep.getCollection(collectionId)
            )
        }
    }

    fun getCollectionPhotos(collectionId: String) {
        viewModelScope.launch(handler) {
            _collectionPhotosLiveDataFlow.postValue(
                photosRep.getCollectionPhotosFlow(collectionId)
            )
        }
    }

    fun likeOrUnlikePhoto(photoId: String, like: Boolean, positionInAdapter: Int) {
        viewModelScope.launch(handler) {
            val likeInfo =
                if (like) photosRep.likePhoto(photoId)
                else photosRep.unlikePhoto(photoId)
            _likeInfoLiveData.postValue(Pair(likeInfo, positionInAdapter))
        }
    }
}