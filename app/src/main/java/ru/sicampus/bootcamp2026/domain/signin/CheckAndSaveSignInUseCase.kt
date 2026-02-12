package ru.sicampus.bootcamp2026.domain.signin

import ru.sicampus.bootcamp2026.data.SignInRepository

class CheckAndSaveSignInUseCase(
    private val signInRepository: SignInRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String
    ): Result<Unit> {
            return signInRepository.checkAndSignIn(login, password).mapCatching { isLogin ->
                if (!isLogin) error("Login or password incorrect")
            }
    }
}