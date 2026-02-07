package ru.sicampus.bootcamp2026.ui.theme.screen

sealed interface ListIntent {
    data object LoadMore: ListIntent
    data object Refresh: ListIntent
}