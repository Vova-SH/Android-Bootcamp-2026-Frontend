package ru.sicampus.bootcamp2026.domain.usecase.user

import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.User

class GetUserByIdUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Long): Result<User> {
        return userRepository.getUserById(userId)
    }
}