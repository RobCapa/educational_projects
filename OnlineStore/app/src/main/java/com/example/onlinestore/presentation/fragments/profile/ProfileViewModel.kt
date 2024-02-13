package com.example.onlinestore.presentation.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.User
import com.example.domain.use_cases.GetFavoriteProductsUseCase
import com.example.domain.use_cases.GetSavedUserUseCase
import com.example.domain.use_cases.RemoveUserUseCase
import com.example.onlinestore.presentation.utils.DEFAULT_USER_ID
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val removeUserUseCase: RemoveUserUseCase,
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
) : ViewModel() {

    private val _userLive = MutableLiveData<User>()
    val userLive: LiveData<User> = _userLive

    private val _countFavoriteProducts = MutableLiveData<Int>()
    val countFavoriteProducts: LiveData<Int> = _countFavoriteProducts

    fun getSavedUser() {
        viewModelScope.launch {
            _userLive.value = getSavedUserUseCase.getSavedUser(DEFAULT_USER_ID)
        }
    }

    fun removeUser(user: User) {
        viewModelScope.launch {
            removeUserUseCase.removeUser(user)
        }
    }

    fun getCountFavoriteProducts() {
        viewModelScope.launch {
            _countFavoriteProducts.value = getFavoriteProductsUseCase.getFavoriteProducts().size
        }
    }
}