package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(dto: UserRegisterDto): Result<UserDto> {
        if (dto.firstName.isBlank()) return Result.failure(IllegalArgumentException("Имя не может быть пустым"))
        if (dto.secondName.isBlank()) return Result.failure(IllegalArgumentException("Фамилия не может быть пустой"))
        if (dto.email.isBlank() || !dto.email.contains("@")) return Result.failure(IllegalArgumentException("Некорректный Email"))
        if (dto.password.length < 4) return Result.failure(IllegalArgumentException("Пароль слишком короткий (минимум 4 символа)"))
        if (dto.password != dto.passwordAgain) return Result.failure(IllegalArgumentException("Пароли не совпадают"))

        return repository.registerUser(dto)
    }
}