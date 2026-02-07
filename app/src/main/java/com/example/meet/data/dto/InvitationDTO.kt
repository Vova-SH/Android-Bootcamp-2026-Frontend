package com.example.meet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InvitationDto(
    @SerialName("id") val id: Long,
    @SerialName("meetingId") val meetingId: Long,
    @SerialName("userId") val userId: Long,
    @SerialName("responseStatus") val responseStatus: String,
    @SerialName("responseComment") val responseComment: String? = null,
    @SerialName("required") val isRequired: Boolean = false,
    @SerialName("respondedAt") val respondedAt: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
)

@Serializable
data class UpdateInvitationDto(
    @SerialName("responseStatus") val responseStatus: String,
    @SerialName("responseComment") val responseComment: String? = null,
)

object InvitationResponseStatus {
    const val PENDING = "PENDING"
    const val ACCEPTED = "ACCEPTED"
    const val DECLINED = "DECLINED"
}

@Serializable
data class InvitationPageDto(
    @SerialName("content") val content: List<InvitationDto>
)
