package com.example.domain.use_cases

import com.example.domain.models.Filters
import com.example.domain.models.Product
import com.example.domain.repositories.ProductRepository

class GetAllProductsUseCase(
    private val repository: ProductRepository,
) {

    suspend fun getAllProducts(filters: Filters): List<Product> {
        return repository.getAllProducts(filters)
    }
}