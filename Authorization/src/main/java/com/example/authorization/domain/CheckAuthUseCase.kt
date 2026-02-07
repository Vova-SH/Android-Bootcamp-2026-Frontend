package com.example.authorization.domain

import com.example.authorization.domain.entites.UserLoginEntity
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: UserLoginEntity) = authRepository.authorize(user)
}