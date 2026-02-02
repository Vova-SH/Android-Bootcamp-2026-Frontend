package ru.sicampus.bootcamp2026.domain.entities

data class CreateMeetingEntity (
    val name: String?,
    val startTime: Byte?,
    val endTime: Byte?,
    val date: String?,
    val participants: List<Int>?,
)