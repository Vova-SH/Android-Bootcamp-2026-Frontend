package ru.innovationcampus.android.domain.meetings.entities

data class MeetingEntity(
    val creatorName: String,
    val date: String,
    val title: String,
    val description: String
)