package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class GetCurrentUserUseCase(
    private val userRepository: UserRepository = UserRepository()
) {
    suspend operator fun invoke(): Result<UserEntity> {
        return userRepository.getCurrentUser()
    }
}
