package ru.sicampus.bootcamp2026.ui.screens.list

sealed interface ListIntent {
    data object LoadMore: ListIntent
    data object Refresh: ListIntent
}
