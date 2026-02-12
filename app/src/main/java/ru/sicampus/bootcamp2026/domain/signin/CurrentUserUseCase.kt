package ru.sicampus.bootcamp2026.domain.signin

import ru.sicampus.bootcamp2026.data.SignInRepository
import ru.sicampus.bootcamp2026.domain.users.entities.UserEntity
import kotlin.math.sign

class CurrentUserUseCase(private val signInRepository: SignInRepository) {
    suspend operator fun invoke(): Result<UserEntity> {
        return signInRepository.getCurrentUser()
    }
}