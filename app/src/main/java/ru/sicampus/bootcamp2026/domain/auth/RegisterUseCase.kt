package ru.sicampus.bootcamp2026.domain.auth

import ru.sicampus.bootcamp2026.data.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String,
        confirmPassword: String,
        name: String,
        lastName: String,
        email: String,
        phoneNumber: String
    ): Result<Unit> {
        if (password != confirmPassword) {
            return Result.failure(Exception("Пароли не совпадают"))
        }

        return repository.register(login, password, name, lastName, email, phoneNumber)
            .map { Unit }
    }
}