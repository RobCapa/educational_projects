package com.example.domain.use_cases

import com.example.domain.models.User
import com.example.domain.repositories.UserRepository

class GetSavedUserUseCase(
    private val repository: UserRepository,
) {

    suspend fun getSavedUser(id: String): User? {
        return repository.getUser(id = id)
    }
}