package ru.sicampus.bootcamp2026.ui.screen.meetings

sealed interface EmployeeListIntent {
    data object LoadMore: EmployeeListIntent
    data object Refresh: EmployeeListIntent
}