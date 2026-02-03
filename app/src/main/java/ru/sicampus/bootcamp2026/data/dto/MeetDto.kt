package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.domain.models.User
import java.time.Instant


@Serializable
data class MeetDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("organizerId")
    val organizerId: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("description")
    val description: String??,
    @SerialName("timeSlot")
    val timeSlot: MeetTimeSlotDto?,
    @SerialName("membersIds")
    val membersIds: Set<Int>?,
    @SerialName("invitedIds")
    val invitedIds: Set<Int>?,
    @SerialName("createdAt")
    val createdAt: String?, // ISO-8601 желательно todo
)

@Serializable
data class MeetTimeSlotDto(
    @SerialName("date")
    val date: String?,
    @SerialName("startHour")
    val startHour: Int?,
    @SerialName("durationHours")
    val durationHours: Int?
)