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
        // 1. Проверка, что все поля заполнены (не пустые и не только пробелы)
        val allFieldsFilled = fullName.trim().isNotBlank() &&
                department.trim().isNotBlank() &&
                position.trim().isNotBlank() &&
                email.trim().isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank()

        if (!allFieldsFilled) return false

        // 2. Проверка формата email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        val isValidEmail = email.trim().matches(emailRegex)

        if (!isValidEmail) return false

        // 3. Проверка минимальной длины пароля
        val isPasswordLengthValid = password.length >= 6

        if (!isPasswordLengthValid) return false

        // 4. КРИТИЧНО: Проверка совпадения паролей
        val passwordsMatch = password == confirmPassword

        if (!passwordsMatch) return false

        // 5. Дополнительная проверка: минимальная длина текстовых полей
        val isFullNameValid = fullName.trim().length >= 2
        val isDepartmentValid = department.trim().length >= 2
        val isPositionValid = position.trim().length >= 2

        return isFullNameValid && isDepartmentValid && isPositionValid
    }
}