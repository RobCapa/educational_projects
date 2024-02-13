package com.example.onlinestore.di.modules

import com.example.data.database.dao.FavoriteProductDao
import com.example.data.database.dao.UserDao
import com.example.data.network.api.StoreApi
import com.example.data.repositories.ProductRepositoryImpl
import com.example.data.repositories.UserRepositoryImpl
import com.example.domain.repositories.ProductRepository
import com.example.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        DatabaseModule::class,
        NetworkModule::class,
    ]
)
class RepositoryModule {

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao = userDao)
    }

    @Provides
    fun provideProductRepository(
        storeApi: StoreApi,
        favoriteProductDao: FavoriteProductDao,
    ): ProductRepository {
        return ProductRepositoryImpl(
            storeApi = storeApi,
            favoriteProductDao = favoriteProductDao,
        )
    }
}