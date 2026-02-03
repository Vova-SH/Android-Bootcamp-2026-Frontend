package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class InviteDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("meetId")
    val meetId: Int?,
    @SerialName("inviterUserId")
    val inviterUserId: Int?,
    @SerialName("invitedUserId")
    val invitedUserId: Int?,
    @SerialName("status")
    val status: InviteStatusDto?,
    @SerialName("createdAt")
    val createdAt: String?,
)

@Serializable
enum class InviteStatusDto {
    @SerialName("PENDING")
    PENDING,

    @SerialName("ACCEPTED")
    ACCEPTED,

    @SerialName("DECLINED")
    DECLINED
}