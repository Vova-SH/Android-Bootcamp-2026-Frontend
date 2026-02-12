package com.teto.planner.data.remote.dto.meeting

import kotlinx.serialization.Serializable

@Serializable
data class MeetingUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val roomId: String? = null,
    val status: String? = null
)