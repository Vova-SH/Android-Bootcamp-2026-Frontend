package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.network.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class GetAuthUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserEntity> {
        return userRepository.getAuthUser()
    }
}

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(fio: String): Result<UserEntity?> {
        return userRepository.getUser(fio)
    }
}