package com.example.myunsplash.di.modules

import com.example.myunsplash.network.Network
import com.example.myunsplash.repositories.UnsplashAuthRepository
import com.example.myunsplash.repositories.UnsplashPhotosRepository
import com.example.myunsplash.repositories.UnsplashUserRepository
import com.example.myunsplash.di.interfaces.AuthRepository
import com.example.myunsplash.di.interfaces.GetApi
import com.example.myunsplash.di.interfaces.PhotosRepository
import com.example.myunsplash.di.interfaces.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {
    @Provides
    @Singleton
    fun getPhotosRepository(impl: UnsplashPhotosRepository): PhotosRepository = impl

    @Provides
    @Singleton
    fun getUserRepository(impl: UnsplashUserRepository): UserRepository = impl

    @Provides
    @Singleton
    fun getAuthRepository(impl: UnsplashAuthRepository): AuthRepository = impl

    @Provides
    @Singleton
    fun getNetwork(impl: Network): GetApi = impl
}