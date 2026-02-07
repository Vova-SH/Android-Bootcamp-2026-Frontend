package ru.sicampus.bootcamp2026.android.domain.auth

class CheckAuthFormatUseCase {
    operator fun invoke(
        login: String,
        password: String
    ): Boolean {

        val isPasswordValid = password.isNotBlank()

        val isLoginValid = login.trim().length >= 3

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        val isEmailFormat = login.trim().matches(emailRegex)

        return isLoginValid && isPasswordValid && isEmailFormat
    }
}