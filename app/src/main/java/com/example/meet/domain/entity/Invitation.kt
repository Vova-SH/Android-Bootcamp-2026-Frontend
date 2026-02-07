package com.example.meet.domain.entity

data class Invitation(
    val id: Int,
    val meetingId: Int,
    val userId: Int,
    val responseStatus: String,
    val isRequired: Boolean = false,
)