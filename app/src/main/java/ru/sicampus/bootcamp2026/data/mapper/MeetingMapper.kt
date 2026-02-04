package ru.sicampus.bootcamp2026.data.mapper

import ru.sicampus.bootcamp2026.data.dto.response.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.response.OrganizerInfoDto
import ru.sicampus.bootcamp2026.data.dto.response.ParticipantInfoDto
import ru.sicampus.bootcamp2026.data.dto.response.ParticipantStatus as ParticipantStatusDto
import ru.sicampus.bootcamp2026.data.dto.response.MeetingStatus as MeetingStatusDto
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.model.MeetingStatus
import ru.sicampus.bootcamp2026.domain.model.OrganizerInfo
import ru.sicampus.bootcamp2026.domain.model.ParticipantInfo
import ru.sicampus.bootcamp2026.domain.model.ParticipantStatus
import java.time.LocalDate

fun MeetingDto.toDomain(): Meeting = Meeting(
    id = id,
    title = title,
    description = description,
    date = LocalDate.parse(date),
    startTime = startTime,
    endTime = endTime,
    location = location,
    status = status.toDomain(),
    organizer = organizer.toDomain(),
    participants = participants.map { it.toDomain() }
)

fun MeetingStatusDto.toDomain(): MeetingStatus = when (this) {
    MeetingStatusDto.SCHEDULED -> MeetingStatus.SCHEDULED
    MeetingStatusDto.CANCELLED -> MeetingStatus.CANCELLED
    MeetingStatusDto.COMPLETED -> MeetingStatus.COMPLETED
}

fun OrganizerInfoDto.toDomain(): OrganizerInfo = OrganizerInfo(
    id = id,
    username = username,
    email = email
)

fun ParticipantInfoDto.toDomain(): ParticipantInfo = ParticipantInfo(
    id = id,
    username = username,
    email = email,
    status = status.toDomain()
)

fun ParticipantStatusDto.toDomain(): ParticipantStatus = when (this) {
    ParticipantStatusDto.PENDING -> ParticipantStatus.PENDING
    ParticipantStatusDto.CONFIRMED -> ParticipantStatus.CONFIRMED
    ParticipantStatusDto.DECLINED -> ParticipantStatus.DECLINED
}

