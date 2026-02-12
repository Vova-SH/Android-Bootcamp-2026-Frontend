package ru.sicampus.bootcamp2026.domain.profile

import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.data.UserRepository

class ChangeUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        id: Int,
        email: String,
        fullname: String
    ): Result<Unit>{
        return userRepository.changeUser(id,email,fullname)
    }
}