package ru.sicampus.bootcamp2026.ui.screens.users

sealed interface UsersIntent {
    data object LoadMore: UsersIntent
    data object Refresh: UsersIntent
}