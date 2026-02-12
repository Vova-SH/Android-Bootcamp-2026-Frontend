package ru.sicampus.bootcamp2026.ui.screens.incomingbooks

import ru.sicampus.bootcamp2026.domain.entities.Invitation

data class IncomingUiState(
    val invitations: List<Invitation> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)