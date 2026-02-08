package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.network.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<UserEntity>> {
        return userRepository.getUsers()
    }
}