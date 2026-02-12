package ru.sicampus.bootcamp2026.domain.entities

data class CreateMeetingEntity(
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
)