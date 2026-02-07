package ru.sicampus.bootcamp2026.domain.auth

import ru.sicampus.bootcamp2026.ui.screen.auth.register.RegisterFields

class ValidateRegistrationDataUseCase {
    operator fun invoke(fields: RegisterFields): ValidationResult {

        val errors = mutableMapOf<String, String>()

        if (fields.name.isBlank()) errors["name"] = "Заполните это поле"
        if (fields.username.length < 4) errors["username"] = "Минимум 4 символа"
        if (!fields.email.contains("@")) errors["email"] = "Неверный формат почты"
        if (!Regex("""^\+7\d{10}$""").matches(fields.phoneNumber)) errors["phoneNumber"] =
            "Формат телефона: +79991234567"
        if (fields.password.length < 10) errors["password"] = "Минимум 10 символов"

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
    data class ValidationResult(
        val isValid: Boolean,
        val errors: Map<String, String> = emptyMap()
    )
}