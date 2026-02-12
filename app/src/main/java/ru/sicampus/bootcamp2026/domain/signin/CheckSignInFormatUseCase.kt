package ru.sicampus.bootcamp2026.domain.signin

class CheckSignInFormatUseCase {
    operator fun invoke(
        login: String,
        password: String
    ): Boolean {
        return login.length > 2 && login.all { char ->
            char.isLetterOrDigit() &&
                    ((char in 'A'..'Z') ||
                            (char in 'a'..'z') ||
                            char.isDigit())
        }
    }
}