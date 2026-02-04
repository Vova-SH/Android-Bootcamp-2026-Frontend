package ru.sicampus.bootcamp2026.data.dto.response

data class UserProfileResponseDto(
    val id: String, //UUID на сервере
    val username: String,
    val email: String,
    val avatarUrl: String? // ссылка на аватарку может и не быть
)
