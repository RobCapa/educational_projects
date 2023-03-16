package com.example.myunsplash.di.interfaces

import com.example.myunsplash.data.User

interface UserRepository {
    suspend fun getCurrentUser(): User
    suspend fun getUser(username: String): User
}