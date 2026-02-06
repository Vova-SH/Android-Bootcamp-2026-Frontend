package ru.sicampus.bootcamp2026.ui.notification

import ru.sicampus.bootcamp2026.domain.model.Invitation
import java.util.UUID

/**
 * UI состояние для экрана уведомлений/приглашений
 */
data class NotificationUiState(
    val invitations: List<Invitation> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

/**
 * События UI для экрана уведомлений
 */
sealed interface NotificationUiEvent {
    data object LoadInvitations : NotificationUiEvent
    data object RefreshInvitations : NotificationUiEvent
    data class AcceptInvitation(val invitationId: UUID) : NotificationUiEvent
    data class DeclineInvitation(val invitationId: UUID) : NotificationUiEvent
    data object DismissError : NotificationUiEvent
    data object DismissSuccess : NotificationUiEvent
}

