package com.example.myunsplash.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class ViewModelWithHandler : ViewModel() {
    private val _exception = SingleLiveEvent<Throwable>()
    val exception: LiveData<Throwable>
        get() = _exception

    protected val handler = CoroutineExceptionHandler { _, ex ->
        _exception.postValue(ex)
    }
}