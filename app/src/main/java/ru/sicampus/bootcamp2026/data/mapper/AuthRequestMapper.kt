package ru.sicampus.bootcamp2026.data.mapper

import ru.sicampus.bootcamp2026.data.dto.request.LoginRequestDto
import ru.sicampus.bootcamp2026.data.dto.request.RegisterRequestDto

fun loginRequest(email: String, password: String): LoginRequestDto =
    LoginRequestDto(
        email = email,
        password = password
    )

fun registerRequest(username: String, email: String, password: String): RegisterRequestDto =
    RegisterRequestDto(
        username = username,
        email = email,
        password = password
    )

