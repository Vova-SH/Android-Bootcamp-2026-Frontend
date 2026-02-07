package com.teto.planner.data.remote.dto.meeting

import com.teto.planner.data.remote.dto.room.RoomDto
import com.teto.planner.data.remote.dto.room.toDomain
import com.teto.planner.data.remote.dto.user.UserSummaryDto
import com.teto.planner.data.remote.dto.user.toDomain
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.model.meeting.MeetingParticipant
import com.teto.planner.domain.model.meeting.MeetingStatus
import com.teto.planner.domain.model.user.ParticipantRole
import com.teto.planner.domain.model.user.ParticipantStatus
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Serializable
data class MeetingDto(
    val id: String,
    val organizer: UserSummaryDto,
    val title: String,
    val description: String?,
    val meetingDate: String,
    val startHour: Int,
    val durationHours: Int,
    val status: String,
    val room: RoomDto? = null,
    val participants: List<MeetingParticipantDto>? = null,
    val createdAt: String? = null,
    val     updatedAt: String? = null
)

@Serializable
data class MeetingParticipantDto(
    val user: UserSummaryDto,
    val role: String,
    val status: String,
    val meetingDate: String? = null,
    val startHour: Int? = null,
    val createdAt: String? = null,
    val respondedAt: String? = null
)


fun MeetingDto.toDomain(): Meeting {
    return Meeting(
        id = id,
        organizer = organizer.toDomain(),
        title = title,
        description = description,
        date = LocalDate.parse(meetingDate),
        startTime = LocalTime.of(startHour, 0),
        durationHours = durationHours,
        room = room?.toDomain(),
        status = MeetingStatus.valueOf(status),
        participants = participants?.map { it.toDomain() } ?: emptyList(),
        createdAt = createdAt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) },
        updatedAt = updatedAt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
    )
}

fun MeetingParticipantDto.toDomain() = MeetingParticipant(
    user = user.toDomain(),
    role = ParticipantRole.valueOf(role),
    status = ParticipantStatus.valueOf(status),
    joinedAt = createdAt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) },
    respondedAt = respondedAt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
)