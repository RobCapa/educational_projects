package com.example.myunsplash.fragments.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.Photo
import com.example.myunsplash.di.interfaces.PhotosRepository
import com.example.myunsplash.utils.ViewModelWithHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photosRep: PhotosRepository
) : ViewModelWithHandler() {
    private val _photosLiveDataFlow = MutableLiveData<Flow<PagingData<Photo>>>()
    val photosLiveDataFlow: LiveData<Flow<PagingData<Photo>>>
        get() = _photosLiveDataFlow

    private val _likeInfoLiveData = MutableLiveData<Pair<LikeInfo, Int>>()
    val likeInfoLiveData: LiveData<Pair<LikeInfo, Int>>
        get() = _likeInfoLiveData

    fun getPhotosFlow() {
        viewModelScope.launch(handler) {
            _photosLiveDataFlow.postValue(photosRep.getPhotosFlow())
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

    fun searchPhotos(query: String) {
        viewModelScope.launch(handler) {
            _photosLiveDataFlow.postValue(
                photosRep.searchPhotosFlow(query)
            )
        }
    }
}