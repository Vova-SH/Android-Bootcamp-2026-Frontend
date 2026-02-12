package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.data.dto.costilSerializers.DateSerializer
import ru.sicampus.bootcamp2026.data.dto.costilSerializers.TimeSerializer
import java.sql.Time
import java.util.Date

@Serializable
data class MeetingDTO (
    @SerialName("title")
    val title: String?,
    @Serializable(with = DateSerializer::class)
    @SerialName("date")
    val date: Date?,
    @Serializable(with = TimeSerializer::class)
    @SerialName("startTime")
    val startTime: Time?,
    @Serializable(with = TimeSerializer::class)
    @SerialName("endTime")
    val endTime: Time?,
    @SerialName("creatorId")
    val creatorId: Int?,
    @SerialName("id")
    val id: Int?,
    )