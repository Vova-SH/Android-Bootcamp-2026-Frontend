package com.example.meet.domain.entity

data class Notification(
    val id: Int,
    val meetingId: Int? = null,
    val type: String,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String,
)