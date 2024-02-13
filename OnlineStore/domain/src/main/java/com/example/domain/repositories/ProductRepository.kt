package com.example.domain.repositories

import com.example.domain.models.Filters
import com.example.domain.models.Product

interface ProductRepository {
    suspend fun getAllProducts(filters: Filters): List<Product>
    suspend fun getFavoriteProducts(): List<Product>
    suspend fun addProductToFavorites(product: Product)
    suspend fun removeProductFromFavorites(product: Product)
    suspend fun getAllFavoriteProducts(): List<Product>
}