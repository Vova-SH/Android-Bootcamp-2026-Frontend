package com.example.planify.main.features.meetings.domain.schemas

import java.time.LocalDateTime

data class PatchMeetingSchema(
    val name: String? = null,
    val description: String? = null,
    val location: String? = null,
    val startsAt: LocalDateTime? = null,
    val duration: Int? = null
)
