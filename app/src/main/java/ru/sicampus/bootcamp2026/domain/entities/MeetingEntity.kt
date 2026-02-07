package ru.sicampus.bootcamp2026.domain.entities

data class MeetingEntity(
    val id: Int,
    val startTime: Byte,
    val endTime: Byte,
    val name: String,
    val date: String,
    val participants: List<Int>,
)
