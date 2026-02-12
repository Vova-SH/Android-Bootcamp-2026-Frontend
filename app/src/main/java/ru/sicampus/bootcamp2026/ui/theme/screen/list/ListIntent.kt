package ru.sicampus.bootcamp2026.ui.theme.screen.auth.ru.sicampus.bootcamp2026.ui.theme.screen.list

sealed interface ListIntent {
    data object LoadMore: ListIntent
    data object Refresh: ListIntent
}