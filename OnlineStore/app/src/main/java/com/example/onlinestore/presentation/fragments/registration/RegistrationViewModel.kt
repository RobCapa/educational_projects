package com.example.onlinestore.presentation.fragments.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.User
import com.example.domain.use_cases.SaveUserUseCase
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val saveUserUseCase: SaveUserUseCase,
) : ViewModel() {

    private val _userWasSavedLive = MutableLiveData(false)
    val userWasSavedLive: LiveData<Boolean> = _userWasSavedLive

    fun saveUser(user: User) {
        viewModelScope.launch {
            _userWasSavedLive.value = saveUserUseCase.saveUser(user)
        }
    }
}