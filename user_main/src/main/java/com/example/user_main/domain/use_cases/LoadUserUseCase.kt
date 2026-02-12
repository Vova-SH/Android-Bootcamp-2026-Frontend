package com.example.user_main.domain.use_cases

import com.example.user_main.domain.UserRepository
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.loadUser()
}

