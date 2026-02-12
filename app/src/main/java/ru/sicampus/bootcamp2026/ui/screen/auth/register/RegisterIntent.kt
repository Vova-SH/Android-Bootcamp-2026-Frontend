package ru.sicampus.bootcamp2026.ui.screen.auth.register

sealed interface RegisterIntent {
    data object Submit : RegisterIntent
}