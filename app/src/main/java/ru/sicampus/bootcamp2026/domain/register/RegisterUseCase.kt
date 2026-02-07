package ru.sicampus.bootcamp2026.domain.register

import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity

class RegisterUseCase( private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        fullName: String
    ): Result<UserEntity>{
        return userRepository.register(email, password, fullName)
    }
}