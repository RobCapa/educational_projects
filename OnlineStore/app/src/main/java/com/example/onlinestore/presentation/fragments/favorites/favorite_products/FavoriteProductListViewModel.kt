package com.example.onlinestore.presentation.fragments.favorites.favorite_products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Product
import com.example.domain.use_cases.GetFavoriteProductsUseCase
import com.example.domain.use_cases.RemoveProductFromFavoritesUseCase
import kotlinx.coroutines.launch

class FavoriteProductListViewModel(
    private val removeProductFromFavoritesUseCase: RemoveProductFromFavoritesUseCase,
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
) : ViewModel() {

    private val _favoriteProductsLive = MutableLiveData<List<Product>>()
    val favoriteProductsLive: LiveData<List<Product>> = _favoriteProductsLive

    fun getFavoriteProducts() {
        viewModelScope.launch {
            _favoriteProductsLive.value = getFavoriteProductsUseCase.getFavoriteProducts()
        }
    }

    fun removeProductFromFavorites(product: Product) {
        viewModelScope.launch {
            removeProductFromFavoritesUseCase.removeProductFromFavorites(product)
        }
    }
}