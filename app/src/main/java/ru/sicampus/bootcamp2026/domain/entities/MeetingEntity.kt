package ru.sicampus.bootcamp2026.domain.entities

data class MeetingEntity(
    val id: Int,
    val name: String,
    val description: String,
    val startTime: String,
    val ownerName: String
)
