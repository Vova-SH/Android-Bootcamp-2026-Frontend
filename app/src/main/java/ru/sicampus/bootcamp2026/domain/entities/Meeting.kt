package ru.sicampus.bootcamp2026.domain.entities

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Meeting(
    val id: Long,
    val title: String,
    val address: String,
    val description: String? = null,
    val date: LocalDate,
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val organizer: UserMini,
    val participants: List<UserMiniInvitation> = emptyList(),
    val createdAt: LocalDateTime
)

data class MeetingMini(
    val id: Long,
    val title: String,
    val description: String? = null,
    val address: String,
    val date: LocalDate,
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val organizer: UserMini
)

data class MeetingCreate(
    val title: String,
    val description: String? = null,
    val address: String,
    val date: String,
    val timeStart: LocalDateTime,
    val timeEnd: LocalDateTime,
    val organizerId: Long,
    val participantIds: List<Long> = emptyList()
)