package com.example.domain.use_cases

import com.example.domain.models.Product
import com.example.domain.repositories.ProductRepository

class RemoveProductFromFavoritesUseCase(
    private val repository: ProductRepository,
) {

    suspend fun removeProductFromFavorites(product: Product) {
        repository.removeProductFromFavorites(product)
    }
}