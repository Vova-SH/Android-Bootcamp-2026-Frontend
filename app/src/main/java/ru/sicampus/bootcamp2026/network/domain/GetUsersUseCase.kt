package ru.sicampus.bootcamp2026.network.domain

import ru.sicampus.bootcamp2026.network.data.UserRepository
import ru.sicampus.bootcamp2026.network.domain.entities.UserEntity

class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<UserEntity>> {
        return userRepository.getUsers()
    }
}

