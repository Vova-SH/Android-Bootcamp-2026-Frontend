package ru.sicampus.bootcamp2026.domain.auth


class CheckAuthFormatUseCase {
    operator fun invoke(email: String, password: String): Boolean {
        val isEmailValid = email.isNotBlank() &&
                email.contains("@") &&
                email.contains(".") &&
                email.length >= 6
        val isPasswordValid = password.length >= 8
        return isEmailValid && isPasswordValid
    }
}