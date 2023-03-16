package com.example.myunsplash.fragments.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myunsplash.di.interfaces.AuthRepository
import com.example.myunsplash.utils.ViewModelWithHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRep: AuthRepository,
) : ViewModelWithHandler() {
    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean>
        get() = _authResult

    fun processResultAuthRequest(intent: Intent?) {
        viewModelScope.launch(handler) {
            if (intent == null) {
                _authResult.value = false
                return@launch
            }
            authRep.processResultAuthRequest(intent) {
                _authResult.postValue(it)
            }
        }
    }
}