package ru.sicampus.bootcamp2026.ui.screen.list

interface ListIntent {
    data class Send(val meetingId: Int, val userId: Int, val response: Boolean): ListIntent
}