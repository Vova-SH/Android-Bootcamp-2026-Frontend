package ru.sicampus.bootcamp2026.domain.usecase.auth

import ru.sicampus.bootcamp2026.data.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.entities.User
import ru.sicampus.bootcamp2026.domain.mapper.UserMapper

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Result<User> {
        return authRepository.register(email, password, firstName, secondName)
    }
}