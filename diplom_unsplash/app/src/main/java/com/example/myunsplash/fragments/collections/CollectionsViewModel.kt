package com.example.myunsplash.fragments.collections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.di.interfaces.PhotosRepository
import com.example.myunsplash.utils.ViewModelWithHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val photosRep: PhotosRepository
) : ViewModelWithHandler() {
    private val _collectionsLiveDataFlow = MutableLiveData<Flow<PagingData<UnsplashCollection>>>()
    val collectionsLiveDataFlow: LiveData<Flow<PagingData<UnsplashCollection>>>
        get() = _collectionsLiveDataFlow

    fun getCollectionsFlow() {
        viewModelScope.launch(handler) {
            _collectionsLiveDataFlow.postValue(photosRep.getCollectionsFlow())
        }
    }

    fun searchCollections(query: String) {
        viewModelScope.launch(handler) {
            _collectionsLiveDataFlow.postValue(
                photosRep.searchCollectionsFlow(query)
            )
        }
    }
}