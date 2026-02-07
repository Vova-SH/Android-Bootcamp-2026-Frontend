package ru.sicampus.bootcamp2026.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.InvitationRepository
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.repository.ProfileRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import javax.inject.Inject

/**
 * ViewModel для главного экрана
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val invitationRepository: InvitationRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
        loadMeetings()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.LoadMeetings -> loadMeetings()
            is HomeUiEvent.RefreshMeetings -> refreshMeetings()
            is HomeUiEvent.FilterByStatus -> filterByStatus(event.status)
            is HomeUiEvent.ChangeSortOrder -> changeSortOrder(event.order)
            is HomeUiEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            when (val result = profileRepository.getProfile()) {
                is Result.Success -> {
                    _uiState.update { it.copy(username = result.data.username) }
                }
                is Result.Error -> {
                    // Не критично, просто не обновим имя
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Загружаем встречи пользователя
            val meetingsResult = meetingRepository.getUserMeetings(
                status = null,
                page = 0,
                size = 50
            )

            // Загружаем приглашения
            val invitationsResult = invitationRepository.getInvitations(0, 10)

            when (meetingsResult) {
                is Result.Success -> {
                    val meetings = meetingsResult.data.content
                    val invitations = when (invitationsResult) {
                        is Result.Success -> invitationsResult.data.content
                        else -> emptyList()
                    }

                    _uiState.update {
                        it.copy(
                            meetings = meetings,
                            invitations = invitations,
                            filteredMeetings = applyFilters(meetings, it.selectedStatus, it.sortOrder),
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to load meetings: ${meetingsResult.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun refreshMeetings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            val meetingsResult = meetingRepository.getUserMeetings(
                status = null,
                page = 0,
                size = 50
            )

            val invitationsResult = invitationRepository.getInvitations(0, 10)

            when (meetingsResult) {
                is Result.Success -> {
                    val meetings = meetingsResult.data.content
                    val invitations = when (invitationsResult) {
                        is Result.Success -> invitationsResult.data.content
                        else -> emptyList()
                    }

                    _uiState.update {
                        it.copy(
                            meetings = meetings,
                            invitations = invitations,
                            filteredMeetings = applyFilters(meetings, it.selectedStatus, it.sortOrder),
                            isRefreshing = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to refresh: ${meetingsResult.exception.message}",
                            isRefreshing = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun filterByStatus(status: String?) {
        val state = _uiState.value
        _uiState.update {
            it.copy(
                selectedStatus = status,
                filteredMeetings = applyFilters(state.meetings, status, state.sortOrder)
            )
        }
    }

    private fun changeSortOrder(order: SortOrder) {
        val state = _uiState.value
        _uiState.update {
            it.copy(
                sortOrder = order,
                filteredMeetings = applyFilters(state.meetings, state.selectedStatus, order)
            )
        }
    }

    private fun applyFilters(
        meetings: List<Meeting>,
        status: String?,
        sortOrder: SortOrder
    ): List<Meeting> {
        var filtered = meetings

        // Фильтр по статусу
        if (status != null) {
            filtered = filtered.filter { it.status.name == status }
        }

        // Сортировка
        filtered = when (sortOrder) {
            SortOrder.INCREASING -> filtered.sortedBy { it.startTime }
            SortOrder.DECREASING -> filtered.sortedByDescending { it.startTime }
        }

        return filtered
    }
}

