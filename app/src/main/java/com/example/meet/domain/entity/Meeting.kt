package com.example.meet.domain.entity

import java.time.LocalDateTime

data class Meeting(
    val id: Int,
    val title: String,
    val description: String? = null,
    val organizerId: Int,
    val roomId: Int? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val meetingPriority: String,
    val status: String,
)