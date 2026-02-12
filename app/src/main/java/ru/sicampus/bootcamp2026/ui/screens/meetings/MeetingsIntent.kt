package ru.sicampus.bootcamp2026.ui.screens.meetings

sealed interface MeetingsIntent {
    data object LoadMore: MeetingsIntent
    data object Refresh: MeetingsIntent
}