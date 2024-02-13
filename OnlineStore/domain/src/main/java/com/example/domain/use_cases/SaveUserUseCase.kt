package com.example.domain.use_cases

import com.example.domain.models.User
import com.example.domain.repositories.UserRepository

class SaveUserUseCase(
    private val repository: UserRepository,
) {

    suspend fun saveUser(user: User): Boolean {
        return repository.saveUser(user)
    }
}