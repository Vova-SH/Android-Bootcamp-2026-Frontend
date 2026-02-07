package ru.sicampus.bootcamp2026.domain.auth

import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.dto.UserDto


class CheckAndSaveAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String,
    ): Result<UserDto> {
        return authRepository.checkAndAuth(login, password).mapCatching { userDto ->
            println("CheckAndSaveAuthUseCase: Starting auth for $login")

//            if(!userEntity) error("Login or pass incorrect")
            userDto

        }
    }
}