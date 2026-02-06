package ru.sicampus.bootcamp2026.ui.meeting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel для экрана деталей встречи
 */
@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeetingDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val meetingId: UUID

    init {
        val meetingIdString = savedStateHandle.get<String>("meetingId") ?: ""
        meetingId = UUID.fromString(meetingIdString)
        loadMeeting()
    }

    fun onEvent(event: MeetingDetailUiEvent) {
        when (event) {
            is MeetingDetailUiEvent.LoadMeeting -> loadMeeting()
            is MeetingDetailUiEvent.CancelMeeting -> cancelMeeting()
            is MeetingDetailUiEvent.DeleteMeeting -> deleteMeeting()
            is MeetingDetailUiEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
            is MeetingDetailUiEvent.DismissSuccess -> {
                _uiState.update { it.copy(successMessage = null) }
            }
        }
    }

    private fun loadMeeting() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = meetingRepository.getMeetingById(meetingId)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            meeting = result.data,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to load meeting: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun cancelMeeting() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = meetingRepository.cancelMeeting(meetingId)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            meeting = result.data,
                            successMessage = "Meeting cancelled successfully",
                            isLoading = false,
                            shouldNavigateBack = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to cancel meeting: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun deleteMeeting() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = meetingRepository.deleteMeeting(meetingId)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            successMessage = "Meeting deleted successfully",
                            isLoading = false,
                            shouldNavigateBack = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to delete meeting: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }
}

