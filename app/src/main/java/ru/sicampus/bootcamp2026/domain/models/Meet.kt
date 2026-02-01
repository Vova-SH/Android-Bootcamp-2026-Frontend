package ru.sicampus.bootcamp2026.domain.models

import java.util.Date

data class Meet(
    val id: Int,
    val purpose: String,
    val description: String,
    val date: Date, // ??
    val organizer: User
)