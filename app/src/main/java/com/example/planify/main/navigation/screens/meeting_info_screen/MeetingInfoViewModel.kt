package com.example.planify.main.navigation.screens.meeting_info_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.main.features.meetings.domain.services.MeetingsService
import com.example.planify.main.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingInfoViewModel @Inject constructor(
    private val meetingService: MeetingsService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val meetingId: Long = checkNotNull(savedStateHandle[AppRoute.MeetingInfoMenu.ARG])

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchMeetingContext(meetingId: Long, refresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.emit(if (refresh) UIState.Refreshing else UIState.Loading)

            meetingService.fetchMeetingContext(meetingId = meetingId)
                .onSuccess { meetingContext ->
                    _uiState.emit(
                        UIState
                            .ContentData(meetingContext)
                    )
                }
                .onFailure { error ->
                    _uiState.emit(UIState.Error(error.message ?: "Runtime error"))
                }
        }
    }

    fun runFetchMeetingContext(meetingId: Long, refresh: Boolean = false) {
        viewModelScope.launch {
            fetchMeetingContext(meetingId, refresh = refresh)
        }
    }

    init {
        fetchMeetingContext(meetingId)
    }
}