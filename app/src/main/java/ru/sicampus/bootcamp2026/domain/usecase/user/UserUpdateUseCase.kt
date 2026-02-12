package ru.sicampus.bootcamp2026.domain.usecase.user

import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.domain.entities.User

class UserUpdateUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: Long,
        firstName: String,
        secondName: String,
        description: String?,
        position: String?,
        department: String?
    ): Result<User> {
        return userRepository.updateUser(userId, firstName, secondName, description, position, department)
    }
}