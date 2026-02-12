package ru.sicampus.bootcamp2026.android.domain.signUp

class CheckSignUpFormatUseCase {
    operator fun invoke(
        fullName: String,
        department: String,
        position: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val allFieldsFilled = fullName.trim().isNotBlank() &&
                department.trim().isNotBlank() &&
                position.trim().isNotBlank() &&
                email.trim().isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank()

        if (!allFieldsFilled) return false

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        val isValidEmail = email.trim().matches(emailRegex)

        if (!isValidEmail) return false

        val isPasswordLengthValid = password.length >= 6

        if (!isPasswordLengthValid) return false

        val passwordsMatch = password == confirmPassword

        if (!passwordsMatch) return false

        val isFullNameValid = fullName.trim().length >= 2
        val isDepartmentValid = department.trim().length >= 2
        val isPositionValid = position.trim().length >= 2

        return isFullNameValid && isDepartmentValid && isPositionValid
    }
}