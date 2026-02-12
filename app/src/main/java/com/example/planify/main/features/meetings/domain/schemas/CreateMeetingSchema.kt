package com.example.planify.main.features.meetings.domain.schemas

import java.time.LocalDateTime

data class CreateMeetingSchema(
    val name: String,
    val description: String,
    val location: String,
    val startsAt: LocalDateTime,
    val duration: Int
)
