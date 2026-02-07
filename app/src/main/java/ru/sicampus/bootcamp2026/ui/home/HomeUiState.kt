package ru.sicampus.bootcamp2026.ui.home

import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.Meeting
import java.util.UUID

/**
 * UI состояние для главного экрана
 */
data class HomeUiState(
    val username: String = "",
    val meetings: List<Meeting> = emptyList(),
    val invitations: List<Invitation> = emptyList(),
    val filteredMeetings: List<Meeting> = emptyList(),

    // Фильтры
    val selectedStatus: String? = null,
    val sortOrder: SortOrder = SortOrder.DECREASING,

    // Состояния
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

/**
 * Порядок сортировки встреч
 */
enum class SortOrder {
    INCREASING,  // По возрастанию даты
    DECREASING   // По убыванию даты
}

/**
 * События UI для главного экрана
 */
sealed interface HomeUiEvent {
    data object LoadMeetings : HomeUiEvent
    data object RefreshMeetings : HomeUiEvent
    data class FilterByStatus(val status: String?) : HomeUiEvent
    data class ChangeSortOrder(val order: SortOrder) : HomeUiEvent
    data object DismissError : HomeUiEvent
}

