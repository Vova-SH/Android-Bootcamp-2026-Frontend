package ru.innovationcampus.android.domain.meetings.entities

data class PagingMeetingListEntity(
    val isLast: Boolean,
    val meetings: List<MeetingEntity>
)