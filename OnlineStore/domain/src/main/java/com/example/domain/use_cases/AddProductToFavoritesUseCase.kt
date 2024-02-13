package com.example.domain.use_cases

import com.example.domain.models.Product
import com.example.domain.repositories.ProductRepository

class AddProductToFavoritesUseCase(
    private val repository: ProductRepository,
) {

    suspend fun addProductToFavorites(product: Product) {
        repository.addProductToFavorites(product)
    }
}