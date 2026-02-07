package ru.sicampus.bootcamp2026.android.domain.signUp

import ru.sicampus.bootcamp2026.android.data.SignUpRepository

class RegisterAndSaveAuthUseCase(
    private val signUpRepository: SignUpRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        fullName: String,
        department: String,
        position: String,
    ): Result<Unit> {
        return signUpRepository.registerAndAuth(
            email = email,
            password = password,
            fullName = fullName,
            department = department,
            position = position
        ).mapCatching { isRegistered ->
            if (!isRegistered) error("Registration failed")
        }
    }
}