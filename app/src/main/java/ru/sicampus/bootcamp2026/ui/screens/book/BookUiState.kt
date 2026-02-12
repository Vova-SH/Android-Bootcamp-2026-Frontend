package ru.sicampus.bootcamp2026.ui.screens.book

import ru.sicampus.bootcamp2026.domain.entities.UserMini
import java.time.LocalDate
import java.time.LocalTime

data class BookUiState(
    val title: String = "",
    val description: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedStartTime: LocalTime = LocalTime.now().withMinute(0).plusHours(2),
    val selectedEndTime: LocalTime = LocalTime.now().withMinute(0).plusHours(3),
    val cabinet: String = "",
    val selectedUsers: List<Long> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<UserMini> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    var currentSearchPage: Int = 0,
    val pageSize: Int = 10,
    var isLastPage: Boolean = false,
    var isSearching: Boolean = false
)