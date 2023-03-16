package com.example.myunsplash.repositories

import com.example.myunsplash.data.User
import com.example.myunsplash.di.interfaces.GetApi
import com.example.myunsplash.di.interfaces.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UnsplashUserRepository @Inject constructor(
    private val network: GetApi,
) : UserRepository {
    override suspend fun getCurrentUser(): User {
        return withContext(Dispatchers.IO) {
            network.getUnsplashApi().getCurrentUser()
        }
    }

    override suspend fun getUser(username: String): User {
        return withContext(Dispatchers.IO) {
            network.getUnsplashApi().getUser(username)
        }
    }
}