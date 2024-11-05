package com.bryson.testapp.data.model

import com.bryson.testapp.data.local.UserDao
import com.bryson.testapp.data.repository.User
import com.bryson.testapp.domain.repository.UserRepository

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override suspend fun registerUser(username: String, password: String): Boolean {
        val existingUser = userDao.getUserByUsername(username)
        return if (existingUser == null) {
            userDao.insert(User(username = username, password = password))
            true
        } else {
            false
        }
    }

    override suspend fun authenticate(username: String, password: String): Boolean {
        val user = userDao.getUserByUsername(username)
        return user != null && user.password == password
    }
}