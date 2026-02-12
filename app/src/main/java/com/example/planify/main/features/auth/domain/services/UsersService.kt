package com.example.planify.main.features.auth.domain.services

import com.example.planify.main.features.auth.entities.User

interface UsersService {
    fun getMe(): User

    suspend fun fetchMe(): User

    suspend fun fetchUsers(userIds: List<Long>): List<User>

    suspend fun fetchUser(userId: Long): User
}