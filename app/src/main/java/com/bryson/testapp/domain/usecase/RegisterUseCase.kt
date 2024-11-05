package com.bryson.testapp.domain.usecase

import com.bryson.testapp.domain.repository.UserRepository

class RegisterUseCase(private val repository: UserRepository) {

    suspend fun execute(username: String, password: String): Boolean {
        return repository.registerUser(username, password)
    }
}