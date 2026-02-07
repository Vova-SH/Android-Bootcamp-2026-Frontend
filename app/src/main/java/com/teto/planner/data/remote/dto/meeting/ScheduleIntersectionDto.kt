package com.teto.planner.data.remote.dto.meeting

import com.teto.planner.data.remote.dto.user.UserSummaryDto
import com.teto.planner.data.remote.dto.user.toDomain
import com.teto.planner.domain.model.meeting.BusySlot
import com.teto.planner.domain.model.meeting.IntersectionResponse
import com.teto.planner.domain.model.meeting.IntersectionSlot
import com.teto.planner.domain.model.meeting.IntersectionSlotStatus
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class BusySlotsResponse(
    val meetingDate: String,
    val busySlots: List<BusySlotDto>
)

@Serializable
data class BusySlotDto(
    val hour: Int,
    val meetingId: String
)

@Serializable
data class IntersectionResponseDto(
    val meetingDate: String,
    val organizer: UserSummaryDto? = null,
    val users: List<UserSummaryDto>,
    val slots: List<IntersectionSlotDto>
)

@Serializable
data class IntersectionSlotDto(
    val hour: Int,
    val status: String,
    val label: String? = null,
    val conflictedUsers: List<UserSummaryDto>? = null
)

fun BusySlotDto.toDomain() = BusySlot(
    hour = hour,
    meetingId = meetingId
)

fun IntersectionResponseDto.toDomain() = IntersectionResponse(
    date = LocalDate.parse(meetingDate),
    organizer = organizer?.toDomain(),
    users = users.map { it.toDomain() },
    slots = slots.map { it.toDomain() }
)

fun IntersectionSlotDto.toDomain() = IntersectionSlot(
    hour = hour,
    status = IntersectionSlotStatus.valueOf(status),
    label = label,
    conflictedUsers = conflictedUsers?.map { it.toDomain() } ?: emptyList()
)