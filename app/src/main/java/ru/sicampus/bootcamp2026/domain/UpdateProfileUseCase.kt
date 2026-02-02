package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class UpdateProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String?,
        phone: String?,
        email: String?,
        info: String?,
        photoUrl: String?
        ): Result<Unit> {
        return userRepository.updateCurrentUser(name, phone, email, info, photoUrl)
    }
}