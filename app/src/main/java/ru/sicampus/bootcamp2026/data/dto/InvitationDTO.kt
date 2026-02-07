package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

@Serializable
data class InvitationDTO(
    @SerialName("id") val id: Int?,
    @SerialName("status") val status: String?,
    @SerialName("message") val message: String?,
    @SerialName("meeting") val meeting: MeetingDTO?,
    )
fun InvitationDTO.toEntity(): InvitationEntity {
    return InvitationEntity(
        id = requireNotNull(id) {"Id is null"},
        status = status.orEmpty(),
        message = message.orEmpty(),
        meeting = meeting?.toEntity() ?: throw IllegalArgumentException("Meeting is null")
    )
}
