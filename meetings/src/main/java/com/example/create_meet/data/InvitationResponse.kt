package com.example.create_meet.data

import com.example.create_meet.data.dto.InvitationStatus
import kotlinx.serialization.Serializable

@Serializable
data class InvitationResponse(
    val id: String,
    val meetingId: String,
    val meetingTitle: String,
    val meetingStartTime: String,
    val meetingLocation: String,
    val status: InvitationStatus,
    val invitedAt: String,
    val respondedAt: String?
)