package ru.sicampus.bootcamp2026.domain.users.entities

data class PagingMeetingListEntity (
    val isLast: Boolean,
    val meetings: List<MeetingEntity>
)