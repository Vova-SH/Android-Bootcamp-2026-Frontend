package ru.sicampus.bootcamp2026.android.ui.testScreens.home

import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import java.time.LocalDate

data class HomeUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val meetings: List<MeetingResponse> = emptyList(),
    val page: Int = 0,
    val isLast: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val weekCountsByDate: Map<LocalDate, Int> = emptyMap(),
    val monthCountsByDate: Map<LocalDate, Int> = emptyMap(),
    val organizerNames: Map<Long, String> = emptyMap()
)
