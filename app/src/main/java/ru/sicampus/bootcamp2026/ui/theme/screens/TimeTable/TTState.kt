package ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable

sealed interface TTState {
    data class Error(val reason: String): TTState
    data object Loading: TTState
    data object Content: TTState
}