package ru.sicampus.bootcamp2026.ui.screens.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.entities.MeetingCreate
import ru.sicampus.bootcamp2026.domain.entities.UserMini
import ru.sicampus.bootcamp2026.domain.usecase.meeting.CreateMeetingUseCase
import ru.sicampus.bootcamp2026.domain.usecase.user.SearchUserUseCase
import ru.sicampus.bootcamp2026.utils.SettingsUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class BookViewModel(
    private val createMeetingUseCase: CreateMeetingUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val settingsUtils: SettingsUtils
) : ViewModel() {

    private val _state = MutableStateFlow(BookUiState())
    val state: StateFlow<BookUiState> = _state.asStateFlow()

    fun onTitleChange(title: String) {
        _state.update { it.copy(title = title, errorMessage = null) }
    }

    fun onDescriptionChange(description: String) {
        _state.update { it.copy(description = description, errorMessage = null) }
    }

    fun onDateChange(date: LocalDate) {
        _state.update { it.copy(selectedDate = date, errorMessage = null) }
    }

    fun onStartTimeChange(time: LocalTime) {
        _state.update { it.copy(selectedStartTime = time, errorMessage = null) }
    }

    fun onEndTimeChange(time: LocalTime) {
        _state.update { it.copy(selectedEndTime = time, errorMessage = null) }
    }

    fun onCabinetChange(cabinet: String) {
        _state.update { it.copy(cabinet = cabinet, errorMessage = null) }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query, errorMessage = null) }

        if (state.value.searchQuery.isNotEmpty() ) {
            _state.update { it.copy(currentSearchPage = 0, isLastPage = false, searchResults = emptyList()) }
            searchUsers(query, true)
        }else{
            _state.update { it.copy(currentSearchPage = 0, isLastPage = false, searchResults = emptyList()) }
            searchUsers("/|", true)
        }
    }

    fun searchUsers(query: String = _state.value.searchQuery, reset: Boolean = false) {
        if (query.isEmpty() || _state.value.isSearching || _state.value.isLastPage) return

        if (reset) _state.update { it.copy(currentSearchPage = 0, isLastPage = false) }

        _state.update { it.copy(isLoading = true, errorMessage = null, isSearching = true) }

        viewModelScope.launch {
            val result = searchUserUseCase(query, _state.value.currentSearchPage, _state.value.pageSize)

            result.fold(
                onSuccess = { users ->
                    val currentResults = if (reset) emptyList() else _state.value.searchResults
                    val newResults = currentResults + users

                    _state.update {
                        it.copy(
                            isLoading = false,
                            searchResults = newResults.distinctBy { user -> user },
                            isSearching = false
                        )
                    }

                    _state.value.currentSearchPage++
                    _state.value.isLastPage = users.size < _state.value.pageSize
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Ошибка поиска пользователей",
                            isSearching = false
                        )
                    }
                }
            )

            _state.value.isSearching = false
        }
    }

    fun addParticipant(user: UserMini) {
        if (!_state.value.selectedUsers.any { it == user.id }) {
            _state.update {
                it.copy(
                    selectedUsers = _state.value.selectedUsers + user.id
                )
            }
        }
    }

    fun removeParticipant(userId: Long) {
        _state.update {
            it.copy(
                selectedUsers = _state.value.selectedUsers.filter { it != userId }
            )
        }
    }

    fun createMeeting() {
        val userId = settingsUtils.getUserId()
        if (userId == -1L) {
            _state.update { it.copy(errorMessage = "Пользователь не авторизован") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val meetingData = MeetingCreate(
                title = _state.value.title,
                description = _state.value.description,
                address = _state.value.cabinet,
                date = _state.value.selectedDate.toString(),
                timeStart = LocalDateTime.of(_state.value.selectedDate, _state.value.selectedStartTime),
                timeEnd = LocalDateTime.of(_state.value.selectedDate, _state.value.selectedEndTime),
                organizerId = userId,
                participantIds = _state.value.selectedUsers
            )

            val result = createMeetingUseCase(meetingData)

            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            title = "",
                            description = "",
                            selectedUsers = emptyList(),
                            cabinet = "Не выбрано",
                            searchResults = emptyList(),
                            searchQuery = ""
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Ошибка создания встречи"
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun clearSuccess() {
        _state.update { it.copy(isSuccess = false) }
    }

    fun clearSearch() {
        _state.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList()
            )
        }
        _state.update { it.copy(currentSearchPage = 0, isLastPage = false) }
    }
}