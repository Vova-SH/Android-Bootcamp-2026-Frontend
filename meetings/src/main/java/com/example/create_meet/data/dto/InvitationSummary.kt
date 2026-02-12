package com.example.create_meet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InvitationSummary(
    val inviteeId: String,
    val status: InvitationStatus,
    val invitedAt: String,
    val respondedAt: String? = null
)