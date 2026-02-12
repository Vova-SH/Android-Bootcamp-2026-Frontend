package ru.sicampus.bootcamp2026.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeetingResponse(
    val id: Long,
    val organizerId: Long,
    val title: String,
    val description: String,
    val startAt: String,
    val endAt: String,
    val createdAt: String
)

@Serializable
data class PageResponse<T>(
    val content: List<T>,
    val last: Boolean,
    val number: Int
)

@Serializable
data class MeetingsCountByDateDto(
    val date: String,
    val count: Int
)
