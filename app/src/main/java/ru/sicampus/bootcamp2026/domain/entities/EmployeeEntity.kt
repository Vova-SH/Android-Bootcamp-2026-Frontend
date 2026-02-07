package ru.sicampus.bootcamp2026.domain.entities

data class EmployeeEntity(
    val name: String,
    val position: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val photoUrl: String?,
)
