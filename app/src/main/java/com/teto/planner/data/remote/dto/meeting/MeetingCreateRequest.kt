package com.teto.planner.data.remote.dto.meeting

import kotlinx.serialization.Serializable

@Serializable
data class MeetingCreateRequest(
    val meetingDate: String,
    val startHour: Int,
    val durationHours: Int = 1,
    val title: String,
    val description: String?,
    val roomId: String?,
    val participantIds: List<String>
)