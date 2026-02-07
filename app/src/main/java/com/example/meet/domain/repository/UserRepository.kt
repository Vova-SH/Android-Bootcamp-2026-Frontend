package com.example.meet.domain.repository

import com.example.meet.domain.entity.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun createUser(user: User): User
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        position: String,
        department: String
    ): Result<User>
    suspend fun getCurrentUser(): Result<User>
    suspend fun getUser(userId: Int): Result<User>
    fun isLoggedIn(): Boolean
    fun logout()
}