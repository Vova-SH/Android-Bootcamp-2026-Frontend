package com.example.meet.data.repository

import com.example.meet.data.mapper.UserMapper
import com.example.meet.data.source.Network
import com.example.meet.domain.entity.User
import com.example.meet.domain.repository.UserRepository
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class UserRepositoryImpl : UserRepository {

    override suspend fun getUsers(): List<User> {
        return Network.getUsers().map { UserMapper.toDomain(it) }
    }

    override suspend fun createUser(user: User): User {
        val dto = Network.createUser(
            email = user.email,
            password = "",
            fullName = user.fullName
        )
        return UserMapper.toDomain(dto)
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return runCatching {
            val dto = Network.login(email, password)
            UserMapper.toDomain(dto)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        fullName: String,
        position: String,
        department: String
    ): Result<User> {
        return runCatching {
            val dto = Network.register(
                email, password, fullName,
                position.takeIf { it.isNotBlank() },
                department.takeIf { it.isNotBlank() }
            )
            UserMapper.toDomain(dto)
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return runCatching {
            val userId = Network.currentUserId ?: error("Не авторизован")
            val dto = Network.getUserById(userId)
            UserMapper.toDomain(dto)
        }
    }

    override suspend fun getUser(userId: Int): Result<User> {
        return runCatching {
            val dto = Network.getUserById(userId.toLong())
            UserMapper.toDomain(dto)
        }
    }

    override fun isLoggedIn(): Boolean = Network.isLoggedIn

    override fun logout() {
        Network.logout()
    }
}
