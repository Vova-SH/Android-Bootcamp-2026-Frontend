package com.example.planify.main.features.meetings.domain.entities

import java.time.LocalDateTime

data class Meeting(
    val id: Long,
    val ownerId: Long,
    val name: String,
    val description: String,
    val location: String,
    val startsAt: LocalDateTime,
    val duration: Int
)
