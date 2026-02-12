package com.example.registration.domain

import com.example.comon.UserEntity
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(user: UserEntity) = registerRepository.register(user)
}