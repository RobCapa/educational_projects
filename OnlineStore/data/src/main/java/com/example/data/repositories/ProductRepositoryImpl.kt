package com.example.data.repositories

import com.example.data.database.dao.FavoriteProductDao
import com.example.data.models.DFavoriteProduct
import com.example.data.network.api.StoreApi
import com.example.data.utils.DataDomainConverter
import com.example.domain.models.Filters
import com.example.domain.models.Product
import com.example.domain.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val storeApi: StoreApi,
    private val favoriteProductDao: FavoriteProductDao,
) : ProductRepository {

    override suspend fun getAllProducts(filters: Filters): List<Product> {
        return withContext(Dispatchers.IO) {
            val favoriteProductIds = getFavoriteProductIds()

            storeApi.getAllProducts()
                .items
                .map(DataDomainConverter::convertDProductToProduct)
                .filter { product ->
                    if (filters.tags.isEmpty() || filters.tags.contains(Filters.ProductTag.CATALOG)) {
                        true
                    } else {
                        product.tags.any { tag ->
                            val filtersTags = filters.tags.map { it.title }
                            tag in filtersTags
                        }
                    }
                }
                .map { product ->
                    val isFavorite = favoriteProductIds.contains(product.id)
                    product.copy(isFavorite = isFavorite)
                }
                .sortedBy { product ->
                    when (filters.sortingStrategy) {
                        Filters.SortingStrategy.POPULARITY -> -product.feedback.rating
                        Filters.SortingStrategy.PRICE_UP -> product.price.priceWithDiscount.toFloat()
                        Filters.SortingStrategy.PRICE_DOWN -> -product.price.priceWithDiscount.toFloat()
                    }
                }
        }
    }

    override suspend fun getFavoriteProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            getAllProducts(Filters()).filter { it.isFavorite }
        }
    }

    override suspend fun addProductToFavorites(product: Product) {
        with(Dispatchers.IO) {
            DFavoriteProduct(productId = product.id).let { favoriteProduct ->
                favoriteProductDao.addFavoriteProducts(listOf(favoriteProduct))
            }
        }
    }

    override suspend fun removeProductFromFavorites(product: Product) {
        with(Dispatchers.IO) {
            DFavoriteProduct(productId = product.id).let { favoriteProduct ->
                favoriteProductDao.removeFavoriteProducts(listOf(favoriteProduct))
            }
        }
    }

    override suspend fun getAllFavoriteProducts(): List<Product> {
        return with(Dispatchers.IO) {
            val favoriteProductIds = getFavoriteProductIds()

            getAllProducts(Filters()).filter { favoriteProductIds.contains(it.id) }
        }
    }

    private suspend fun getFavoriteProductIds(): List<String> {
        return with(Dispatchers.IO) {
            favoriteProductDao
                .getAllFavoriteProducts()
                .map { it.productId }
        }
    }

}