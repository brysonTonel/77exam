package com.bryson.testapp.domain.repository

interface UserRepository {
    suspend fun registerUser(username: String, password: String): Boolean
    suspend fun authenticate(username: String, password: String): Boolean
}