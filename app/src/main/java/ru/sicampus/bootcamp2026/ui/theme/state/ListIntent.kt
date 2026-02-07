package ru.sicampus.bootcamp2026.ui.theme.state 

sealed interface ListIntent {
    data object LoadMore: ListIntent
    data object Refresh: ListIntent
}