package ru.innovationcampus.android.ui.screen.list

sealed interface ListIntent {
    data object LoadMore: ListIntent
    data object Refresh: ListIntent
}