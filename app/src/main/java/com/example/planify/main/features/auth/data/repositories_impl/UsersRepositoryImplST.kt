package com.example.planify.main.features.auth.data.repositories_impl

import com.example.planify.main.features.auth.domain.repositories.UsersRepository
import com.example.planify.main.features.auth.entities.User

val user = User(
    id = 123L,
    email = "123",
    username = "1234"
)
object UsersRepositoryImplST : UsersRepository {
    override fun getMe(): User {
        return user
    }

    override suspend fun fetchMe(): User {
        return user
    }

    override suspend fun fetchUsers(userIds: List<Long>): List<User> {
        return listOf(user)
    }

    override suspend fun fetchUser(userId: Long): User {
        return user
    }
}