package ru.sicampus.bootcamp2026.domain.home.entities

class ParticipantEntity(
    val id: Int,
    val fullName: String,
    val status: String = "Ожидает"
)