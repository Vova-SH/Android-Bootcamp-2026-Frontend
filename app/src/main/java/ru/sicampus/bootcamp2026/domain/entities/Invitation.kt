package ru.sicampus.bootcamp2026.domain.entities

import kotlinx.serialization.SerialName
import java.time.LocalDate
import java.time.LocalTime

class Invitation (
    val id: Long,
    val authorId: Long,
    val authorFirstName: String,
    val authorSecondName: String,
    val meetingId: Long,
    val title: String,
    val address: String,
    val date: LocalDate,
    val timeStart: LocalTime,
    val timeEnd: LocalTime
)