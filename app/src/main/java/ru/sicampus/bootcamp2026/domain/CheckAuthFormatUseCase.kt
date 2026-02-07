package ru.sicampus.bootcamp2026.domain

class CheckAuthFormatUseCase {
    operator fun invoke(
        email: String,
        password: String
    ): Boolean {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        return trimmedEmail.contains("@") && trimmedEmail.contains(".") && trimmedPassword.isNotEmpty()
    }
}