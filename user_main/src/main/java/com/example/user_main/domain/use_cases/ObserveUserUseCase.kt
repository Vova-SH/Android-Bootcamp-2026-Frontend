package com.example.user_main.domain.use_cases

import com.example.user_main.domain.UserRepository
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke() = userRepository.currentUser

}

