package ru.sicampus.bootcamp2026.data.dto.invitation;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InvitationDto(
    @SerialName("id") val id: Long,
    @SerialName("authorId") val authorId: Long,
    @SerialName("authorFirstName") val authorFirstName: String,
    @SerialName("authorSecondName") val authorSecondName: String,
    @SerialName("meetingId") val meetingId: Long,
    @SerialName("title") val title: String,
    @SerialName("address") val address: String,
    @SerialName("date") val date: String,
    @SerialName("timeStart") val timeStart: String,
    @SerialName("timeEnd") val timeEnd: String
)