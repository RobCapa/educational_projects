package ru.aston.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.aston.interfaces.repositories.FiltersRepository
import ru.aston.repositories.FiltersRepositoryImpl
import javax.inject.Named
import javax.inject.Singleton

@Module(
    includes = [
        ContextModule::class,
    ]
)
class FiltersRepositoryModule {

    @Singleton
    @Provides
    fun bindFiltersRepository(impl: FiltersRepositoryImpl): FiltersRepository = impl

    @Provides
    fun provideFiltersSharedPref(
        context: Context,
        @Named("filters_shared_preferences") sharedPrefKey: String,
    ): SharedPreferences {
        return context.getSharedPreferences(
            sharedPrefKey,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Named("filters_shared_preferences")
    fun provideFiltersSharedPrefKey(): String = "filters_shared_preferences"
}