package com.example.domain.repositories

import com.example.domain.models.User

interface UserRepository {
    suspend fun saveUser(user: User): Boolean
    suspend fun removeUser(user: User): Boolean
    suspend fun getUser(id: String): User?
}