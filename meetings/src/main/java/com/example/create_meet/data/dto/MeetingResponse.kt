package com.example.create_meet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeetingResponse(
    val id: String,
    val organizerId: String,
    val title: String,
    val description: String? = null,
    val location: String,
    val startTime: String,
    val durationHours: Short,
    val status: MeetingStatus,
    val invitations: List<InvitationSummary>
)

