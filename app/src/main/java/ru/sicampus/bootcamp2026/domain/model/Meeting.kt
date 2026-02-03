package ru.sicampus.bootcamp2026.domain.model

import java.time.LocalDate

data class Meeting(
    val id: String, // UUID на сервере
    val title: String,
    val description: String?,
    val date: LocalDate,
    val startTime: String,
    val endTime: String,
    val location: String?,
    val status: MeetingStatus,
    val organizer: OrganizerInfo,
    val participants: List<ParticipantInfo>
)

data class OrganizerInfo(
    val id: String,//UUID на сервере
    val username: String,
    val email: String
)

data class ParticipantInfo(
    val id: String,//UUID на сервере
    val username: String,
    val email: String,
    val status: ParticipantStatus
)

enum class ParticipantStatus {
    PENDING,
    CONFIRMED,
    DECLINED
}
