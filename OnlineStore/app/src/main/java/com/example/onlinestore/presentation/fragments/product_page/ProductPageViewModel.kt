package com.example.onlinestore.presentation.fragments.product_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Product
import com.example.domain.use_cases.AddProductToFavoritesUseCase
import com.example.domain.use_cases.RemoveProductFromFavoritesUseCase
import kotlinx.coroutines.launch

class ProductPageViewModel(
    private val addProductToFavoritesUseCase: AddProductToFavoritesUseCase,
    private val removeProductFromFavoritesUseCase: RemoveProductFromFavoritesUseCase,
) : ViewModel() {

    fun addProductToFavorites(product: Product) {
        viewModelScope.launch {
            addProductToFavoritesUseCase.addProductToFavorites(product)
        }
    }

    fun removeProductFromFavorites(product: Product) {
        viewModelScope.launch {
            removeProductFromFavoritesUseCase.removeProductFromFavorites(product)
        }
    }
}