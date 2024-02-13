package com.example.onlinestore.di.modules

import com.example.domain.repositories.ProductRepository
import com.example.domain.repositories.UserRepository
import com.example.domain.use_cases.AddProductToFavoritesUseCase
import com.example.domain.use_cases.GetAllProductsUseCase
import com.example.domain.use_cases.GetFavoriteProductsUseCase
import com.example.domain.use_cases.GetSavedUserUseCase
import com.example.domain.use_cases.RemoveProductFromFavoritesUseCase
import com.example.domain.use_cases.RemoveUserUseCase
import com.example.domain.use_cases.SaveUserUseCase
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        RepositoryModule::class,
    ]
)
class UseCaseModule {

    @Provides
    fun provideGetSavedUserUseCase(userRepository: UserRepository): GetSavedUserUseCase {
        return GetSavedUserUseCase(repository = userRepository)
    }

    @Provides
    fun provideRemoveUserUseCase(userRepository: UserRepository): RemoveUserUseCase {
        return RemoveUserUseCase(repository = userRepository)
    }

    @Provides
    fun provideAddProductToFavoritesUseCase(productRepository: ProductRepository): AddProductToFavoritesUseCase {
        return AddProductToFavoritesUseCase(repository = productRepository)
    }

    @Provides
    fun provideGetFavoriteProductsUseCase(productRepository: ProductRepository): GetFavoriteProductsUseCase {
        return GetFavoriteProductsUseCase(repository = productRepository)
    }

    @Provides
    fun provideRemoveProductFromFavoritesUseCase(productRepository: ProductRepository): RemoveProductFromFavoritesUseCase {
        return RemoveProductFromFavoritesUseCase(repository = productRepository)
    }

    @Provides
    fun provideSaveUserUseCase(userRepository: UserRepository): SaveUserUseCase {
        return SaveUserUseCase(repository = userRepository)
    }

    @Provides
    fun provideGetAllProductsUseCase(productRepository: ProductRepository): GetAllProductsUseCase {
        return GetAllProductsUseCase(repository = productRepository)
    }
}