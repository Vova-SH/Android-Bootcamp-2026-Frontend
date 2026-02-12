package ru.sicampus.bootcamp2026.domain.users.entities

import kotlinx.serialization.SerialName
import java.sql.Time
import java.util.Date

class MeetingEntity (
    val title: String,
    val date: Date,
    val startTime: Time,
    val endTime: Time,
    val creatorId: Int,
    val id: Int
)