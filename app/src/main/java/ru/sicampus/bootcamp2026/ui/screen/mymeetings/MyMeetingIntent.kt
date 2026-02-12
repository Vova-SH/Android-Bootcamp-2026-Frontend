package ru.sicampus.bootcamp2026.ui.screen.mymeetings

interface MyMeetingIntent {
    data class Send(val meetingId: Int, val userId: Int, val response: Boolean): MyMeetingIntent
}