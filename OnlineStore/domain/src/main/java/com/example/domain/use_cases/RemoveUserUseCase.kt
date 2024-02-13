package com.example.domain.use_cases

import com.example.domain.models.User
import com.example.domain.repositories.UserRepository

class RemoveUserUseCase(
    private val repository: UserRepository,
) {

    suspend fun removeUser(user: User): Boolean {
        return repository.removeUser(user)
    }
}