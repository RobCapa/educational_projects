package com.example.onlinestore.presentation.fragments.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.use_cases.GetSavedUserUseCase
import com.example.onlinestore.presentation.utils.DEFAULT_USER_ID
import kotlinx.coroutines.launch

class StartViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
) : ViewModel() {

    private val _isSavedUserExistsLive = MutableLiveData<Boolean>()
    val isSavedUserExistsLive: LiveData<Boolean> = _isSavedUserExistsLive

    fun isSavedUserExists() {
        viewModelScope.launch {
            _isSavedUserExistsLive.value = getSavedUserUseCase.getSavedUser(DEFAULT_USER_ID) != null
        }
    }
}