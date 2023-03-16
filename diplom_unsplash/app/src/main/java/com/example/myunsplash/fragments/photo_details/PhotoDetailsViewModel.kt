package com.example.myunsplash.fragments.photo_details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.PhotoDetails
import com.example.myunsplash.di.interfaces.PhotosRepository
import com.example.myunsplash.utils.ViewModelWithHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photosRep: PhotosRepository
)  : ViewModelWithHandler() {
    private val _photoLiveData = MutableLiveData<PhotoDetails>()
    val photoLiveData: LiveData<PhotoDetails>
        get() = _photoLiveData

    private val _likeInfoLiveData = MutableLiveData<LikeInfo>()
    val likeInfoLiveData: LiveData<LikeInfo>
        get() = _likeInfoLiveData

    private val _downloadingPhotoInfoLiveData = MutableLiveData<LiveData<WorkInfo>>()
    val downloadingPhotoInfoLiveData: LiveData<LiveData<WorkInfo>>
        get() = _downloadingPhotoInfoLiveData

    fun getPhotoDetails(photoId: String) {
        viewModelScope.launch(handler) {
            _photoLiveData.postValue(photosRep.getPhotoDetails(photoId))
        }
    }

    fun likeOrUnlikePhoto(photoId: String, like: Boolean) {
        viewModelScope.launch(handler) {
            _likeInfoLiveData.postValue(
                if (like) photosRep.likePhoto(photoId)
                else photosRep.unlikePhoto(photoId)
            )
        }
    }

    fun downloadPhoto(url: String) {
        viewModelScope.launch(handler) {
            _downloadingPhotoInfoLiveData.postValue(photosRep.downloadPhoto(url))
        }
    }

    fun shareImageUrl(photoId: String, context: Context) {
        photosRep.shareImageUrl(photoId, context)
    }
}