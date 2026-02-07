package ru.sicampus.bootcamp2026.domain.reg

import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.RegisterResponse
import ru.sicampus.bootcamp2026.data.source.UserPreferences

class RegistrationUseCase(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke(
        fullName: String,
        jobTitle: List<String>,
        email: String,
        password: String,
        passwordConfirm: String,
        department: List<String>
    ): Result<Boolean> {

        userPreferences.saveUserEmail(email)
        val request = RegisterResponse(
            fullName = fullName,
            jobTitle = jobTitle,
            email = email,
            password = password,
            passwordConfirm = passwordConfirm,
            department = department
        )
        return authRepository.register(request)
    }
}

