package com.example.registration.domain

import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(user: UserRegisterEntity) = registerRepository.register(user)
}