package ru.sicampus.bootcamp2026.domain.usecase

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(dto: UserRegisterDto): Result<UserDto> {
        return repository.registerUser(dto)
    }
}