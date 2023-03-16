package com.example.myunsplash.fragments.profile

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.User
import com.example.myunsplash.interfaces.Equatable
import com.example.myunsplash.di.interfaces.AuthRepository
import com.example.myunsplash.di.interfaces.PhotosRepository
import com.example.myunsplash.di.interfaces.UserRepository
import com.example.myunsplash.utils.ViewModelWithHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val photosRep: PhotosRepository,
    private val userRep: UserRepository,
    private val authRep: AuthRepository,
) : ViewModelWithHandler() {
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser

    private val _itemsLiveDataFlow = MutableLiveData<Flow<PagingData<out Equatable>>>()
    val itemsLiveDataFlow: LiveData<Flow<PagingData<out Equatable>>>
        get() = _itemsLiveDataFlow

    private val _likeInfoLiveData = MutableLiveData<Pair<LikeInfo, Int>>()
    val likeInfoLiveData: LiveData<Pair<LikeInfo, Int>>
        get() = _likeInfoLiveData

    fun getCurrentUser() {
        viewModelScope.launch(handler) {
            val currentUser = userRep.getCurrentUser()
            val user = userRep.getUser(currentUser.username)
            _currentUser.postValue(currentUser.copy(avatarUrl = user.avatarUrl))
        }
    }

    fun getUserPhotosFlow(username: String) {
        viewModelScope.launch(handler) {
            _itemsLiveDataFlow.postValue(photosRep.getUserPhotosFlow(username))
        }
    }

    fun getFavoritesPhotosFlow(username: String) {
        viewModelScope.launch(handler) {
            _itemsLiveDataFlow.postValue(photosRep.getFavoritesPhotosFlow(username))
        }
    }

    fun getCollectionsFlow(username: String) {
        viewModelScope.launch(handler) {
            _itemsLiveDataFlow.postValue(photosRep.getUserCollectionsFlow(username))
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

    fun processResultLogoutRequest(intent: Intent?) {
        if (intent != null) {
            authRep.removeToken()
        }
    }
}