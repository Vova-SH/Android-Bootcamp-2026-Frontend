package ru.innovationcampus.android.ui.screen.meetings

sealed interface MeetingsListIntent {
    data object LoadMore: MeetingsListIntent
    data object Refresh: MeetingsListIntent
}