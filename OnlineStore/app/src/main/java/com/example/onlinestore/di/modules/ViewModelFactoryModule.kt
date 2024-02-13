package com.example.onlinestore.di.modules

import com.example.domain.use_cases.AddProductToFavoritesUseCase
import com.example.domain.use_cases.GetAllProductsUseCase
import com.example.domain.use_cases.GetFavoriteProductsUseCase
import com.example.domain.use_cases.GetSavedUserUseCase
import com.example.domain.use_cases.RemoveProductFromFavoritesUseCase
import com.example.domain.use_cases.RemoveUserUseCase
import com.example.domain.use_cases.SaveUserUseCase
import com.example.onlinestore.presentation.fragments.catalog.CatalogViewModel
import com.example.onlinestore.presentation.fragments.favorites.favorite_products.FavoriteProductListViewModel
import com.example.onlinestore.presentation.fragments.product_page.ProductPageViewModel
import com.example.onlinestore.presentation.fragments.profile.ProfileViewModel
import com.example.onlinestore.presentation.fragments.registration.RegistrationViewModel
import com.example.onlinestore.presentation.fragments.start.StartViewModel
import com.example.onlinestore.presentation.utils.ViewModelFactory
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        UseCaseModule::class,
    ]
)
class ViewModelFactoryModule {

    @Provides
    fun provideStartViewModelFactory(
        getSavedUserUseCase: GetSavedUserUseCase,
    ): ViewModelFactory<StartViewModel> {
        return ViewModelFactory {
            StartViewModel(
                getSavedUserUseCase = getSavedUserUseCase,
            )
        }
    }

    @Provides
    fun provideRegistrationViewModelFactory(
        saveUserUseCase: SaveUserUseCase,
    ): ViewModelFactory<RegistrationViewModel> {
        return ViewModelFactory {
            RegistrationViewModel(
                saveUserUseCase = saveUserUseCase,
            )
        }
    }

    @Provides
    fun provideCatalogViewModelFactory(
        getAllProductsUseCase: GetAllProductsUseCase,
        addProductToFavoritesUseCase: AddProductToFavoritesUseCase,
        removeProductFromFavoritesUseCase: RemoveProductFromFavoritesUseCase,
    ): ViewModelFactory<CatalogViewModel> {
        return ViewModelFactory {
            CatalogViewModel(
                getAllProductsUseCase = getAllProductsUseCase,
                addProductToFavoritesUseCase = addProductToFavoritesUseCase,
                removeProductFromFavoritesUseCase = removeProductFromFavoritesUseCase
            )
        }
    }

    @Provides
    fun provideProductPageViewModelFactory(
        addProductToFavoritesUseCase: AddProductToFavoritesUseCase,
        removeProductFromFavoritesUseCase: RemoveProductFromFavoritesUseCase,
    ): ViewModelFactory<ProductPageViewModel> {
        return ViewModelFactory {
            ProductPageViewModel(
                addProductToFavoritesUseCase = addProductToFavoritesUseCase,
                removeProductFromFavoritesUseCase = removeProductFromFavoritesUseCase,
            )
        }
    }

    @Provides
    fun provideFavoritesListViewModelFactory(
        removeProductFromFavoritesUseCase: RemoveProductFromFavoritesUseCase,
        getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    ): ViewModelFactory<FavoriteProductListViewModel> {
        return ViewModelFactory {
            FavoriteProductListViewModel(
                removeProductFromFavoritesUseCase = removeProductFromFavoritesUseCase,
                getFavoriteProductsUseCase = getFavoriteProductsUseCase
            )
        }
    }

    @Provides
    fun provideProfileViewModelFactory(
        getSavedUserUseCase: GetSavedUserUseCase,
        removeUserUseCase: RemoveUserUseCase,
        getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    ): ViewModelFactory<ProfileViewModel> {
        return ViewModelFactory {
            ProfileViewModel(
                getSavedUserUseCase = getSavedUserUseCase,
                removeUserUseCase = removeUserUseCase,
                getFavoriteProductsUseCase = getFavoriteProductsUseCase,
            )
        }
    }
}