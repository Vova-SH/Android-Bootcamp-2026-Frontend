package ru.sicampus.bootcamp2026.data.dto.response


data class MeetingDto(
    val id: String,//UUID на сервере
    val title: String,
    val description: String?,//описания может и не быть
    val location: String?,//локации встречи может и не быть
    val startTime: String,
    val endTime: String,
    val date: String,
    val createdAt: String,
    val updatedAt: String,
    val status: MeetingStatus,
    val organizer: OrganizerInfoDto,
    val participants: List<ParticipantInfoDto>
)

enum class MeetingStatus{
    SCHEDULED,
    CANCELLED,
    COMPLETED}

enum class ParticipantStatus{
    PENDING,
    CONFIRMED,
    DECLINED}

data class OrganizerInfoDto(
    val id: String,//UUID на сервере
    val username: String,
    val email: String
)

data class ParticipantInfoDto(
    val id: String,//UUID на сервере
    val username: String,
    val email: String,
    val status: ParticipantStatus
)

