package ru.sicampus.bootcamp2026.domain.home.entities


class EventEntity (
    val id : Int,
    val title: String,
    val description: String,
    val organizerName: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val participants: List<ParticipantEntity>
)
