package ru.sicampus.bootcamp2026.android.ui.testScreens.home

import ru.sicampus.bootcamp2026.android.data.dto.InvitationWithMeetingDto

data class NotificationsUiState(
    val invites: List<InvitationWithMeetingDto> = emptyList(),
    val page: Int = 0,
    val isLast: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val organizerNames: Map<Long, String> = emptyMap()
)
