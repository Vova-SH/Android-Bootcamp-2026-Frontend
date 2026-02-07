package ru.sicampus.bootcamp2026.domain.entities

import kotlinx.serialization.SerialName

class Invitation (
    val id: Long,
    val authorId: Long,
    val authorFirstName: String,
    val authorSecondName: String,
    val meetingId: Long,
    val title: String,
    val address: String,
    val date: String,
    val timeStart: String,
    val timeEnd: String
)