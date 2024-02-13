package com.example.domain.use_cases

import com.example.domain.models.Product
import com.example.domain.repositories.ProductRepository

class GetFavoriteProductsUseCase(
    private val repository: ProductRepository,
) {

    suspend fun getFavoriteProducts(): List<Product> {
        return repository.getFavoriteProducts()
    }
}