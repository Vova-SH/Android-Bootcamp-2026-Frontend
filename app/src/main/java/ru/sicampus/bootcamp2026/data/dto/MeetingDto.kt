package ru.sicampus.bootcamp2026.data.dto
data class MeetingDto(
    val id: Long? = null,
    val title: String? = null,
    val startAt: String? = null,
    val endAt: String? = null,
    val description: String? = null,
    val type: MeetingTypeDto? = null,
    val location: String? = null,
    val url: String? = null,
    val inviteeLogins: List<String>? = null
)
