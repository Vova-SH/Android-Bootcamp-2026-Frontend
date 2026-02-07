package ru.sicampus.bootcamp2026.data.dto.invitation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.domain.entities.InvitationStatus

@Serializable
data class InvitationRespondDto(
    @SerialName("invitationId") val invitationId: Long,
    @SerialName("status") val status: InvitationStatus,

)