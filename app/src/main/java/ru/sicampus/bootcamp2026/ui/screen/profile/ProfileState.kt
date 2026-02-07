package ru.sicampus.bootcamp2026.ui.screen.profile

import ru.sicampus.bootcamp2026.domain.entities.EmployeeEntity

sealed interface ProfileState {
    data object Loading : ProfileState
    data class Content(
        val user : EmployeeEntity
    ) : ProfileState
    data class Error(
        val reason: String
    ) : ProfileState
}