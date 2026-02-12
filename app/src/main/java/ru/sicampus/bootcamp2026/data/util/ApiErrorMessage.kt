package ru.sicampus.bootcamp2026.data.util

import retrofit2.HttpException


fun Throwable.toUiMessage(): String {
    return when (this) {
        is HttpException -> {
            val code = code()
            val rawBody = runCatching { response()?.errorBody()?.string() }.getOrNull()
                ?.trim()
                ?.takeIf { it.isNotBlank() }

            when {
                code == 401 -> rawBody ?: "Неверный логин или пароль"
                rawBody != null -> rawBody
                else -> "Ошибка HTTP $code"
            }
        }

        else -> message?.takeIf { it.isNotBlank() } ?: "Неизвестная ошибка"
    }
}
