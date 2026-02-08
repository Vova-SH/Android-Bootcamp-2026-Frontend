package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.repository.UserRepository

class GetUserByEmailUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String): Result<UserDto> {
        if (email.isBlank()) return Result.failure(IllegalArgumentException("Email не может быть пустым"))
        return repository.getUserByEmail(email)
    }
}