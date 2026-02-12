package com.example.planify.main.features.auth.domain.services_impl

import com.example.planify.core.SingletonHolder
import com.example.planify.main.features.auth.domain.repositories.UsersRepository
import com.example.planify.main.features.auth.domain.services.UsersService
import com.example.planify.main.features.auth.entities.User

class UsersServiceImplST private constructor(
    val usersRepository: UsersRepository
) : UsersService {
    override fun getMe(): User {
        return usersRepository.getMe()
    }

    override suspend fun fetchMe(): User {
        return usersRepository.fetchMe()
    }

    override suspend fun fetchUsers(userIds: List<Long>): List<User> {
        return usersRepository.fetchUsers(userIds = userIds)
    }

    override suspend fun fetchUser(userId: Long): User {
        return usersRepository.fetchUser(userId = userId)
    }

    companion object : SingletonHolder<UsersServiceImplST, UsersRepository>(::UsersServiceImplST)
}