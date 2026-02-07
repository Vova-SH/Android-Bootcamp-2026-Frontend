package ru.sicampus.bootcamp2026.domain.home.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class InvitationEntity(
    @SerialName("meetingId")
    val meetingId: Int,

    @SerialName("userId")
    val userId: Int,

    @SerialName("response")
    val response: Boolean
)