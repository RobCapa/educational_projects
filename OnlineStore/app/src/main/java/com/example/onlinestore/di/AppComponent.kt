package com.example.onlinestore.di

import android.app.Application
import com.example.onlinestore.di.modules.ViewModelFactoryModule
import com.example.onlinestore.presentation.activite.MainActivity
import com.example.onlinestore.presentation.fragments.catalog.CatalogFragment
import com.example.onlinestore.presentation.fragments.favorites.favorite_products.FavoriteProductListFragment
import com.example.onlinestore.presentation.fragments.product_page.ProductPageFragment
import com.example.onlinestore.presentation.fragments.profile.ProfileFragment
import com.example.onlinestore.presentation.fragments.registration.RegistrationFragment
import com.example.onlinestore.presentation.fragments.start.StartFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelFactoryModule::class,
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(frag: RegistrationFragment)
    fun inject(frag: CatalogFragment)
    fun inject(frag: ProfileFragment)
    fun inject(frag: FavoriteProductListFragment)
    fun inject(frag: ProductPageFragment)
    fun inject(frag: StartFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}