package com.example.onlinestore.presentation.fragments.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Filters
import com.example.domain.models.Product
import com.example.domain.use_cases.AddProductToFavoritesUseCase
import com.example.domain.use_cases.GetAllProductsUseCase
import com.example.domain.use_cases.RemoveProductFromFavoritesUseCase
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductToFavoritesUseCase: AddProductToFavoritesUseCase,
    private val removeProductFromFavoritesUseCase: RemoveProductFromFavoritesUseCase,
) : ViewModel() {

    private val _productsLive = MutableLiveData<List<Product>>()
    val productsLive: LiveData<List<Product>> = _productsLive

    fun getAllProducts(filters: Filters) {
        viewModelScope.launch {
            _productsLive.value = getAllProductsUseCase.getAllProducts(filters)
        }
    }

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