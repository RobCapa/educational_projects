package ru.aston.dagger.modules

import dagger.Binds
import dagger.Module
import ru.aston.interfaces.repositories.NewsRepository
import ru.aston.repositories.NewsRepositoryImpl

@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class,
        ContextModule::class,
    ]
)
abstract class NewsRepositoryModule {
    @Binds
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository
}