package ru.sicampus.bootcamp2026.domain.models

data class User(
    val id: String,
    val login: String,
    val firstName: String,
    val lastName: String?,
    val patronymic: String?,
    val age: Int,
    val gender: Gender,
    val bio: String?,
    val avatar: String, // todo
    val job: String, // maybe
    val lastActive: Long,
)

enum class Gender {
    MALE, FEMALE
}

