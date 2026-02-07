package ru.sicampus.bootcamp2026.domain.usecases.users

import ru.sicampus.bootcamp2026.data.repos.UserRepository
import ru.sicampus.bootcamp2026.domain.models.User

class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
    }
}