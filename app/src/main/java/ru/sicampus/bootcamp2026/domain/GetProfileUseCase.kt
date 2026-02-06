package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class GetProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserEntity>? {
        return userRepository.getCurrentUser()
    }
}