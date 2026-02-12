package com.example.planify.main.navigation.screens.main_screen.views.home.home_view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.core.ui.state.ResourceState
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import com.example.planify.main.features.meetings.domain.services.MeetingsService
import com.example.planify.main.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class HomeViewModel @Inject constructor(
    val meetingsService: MeetingsService
) : ViewModel() {
    private val _navigateEvents = MutableSharedFlow<AppRoute>()
    val navigateEvents = _navigateEvents.asSharedFlow()

    private val _uiState = MutableStateFlow(UIState.empty())
    val uiState = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun meetingClick(meetingId: Long) {
        viewModelScope.launch {
            _navigateEvents.emit(AppRoute.MeetingInfoMenu(meetingId))
        }
    }

    suspend fun fetchMeetingsInfo(start: LocalDate, end: LocalDate, reset: Boolean = false) {
        val currentData = (_uiState.value.meetingsInfo as? ResourceState.Success)?.data ?: emptyMap()

        _uiState.update { it.copy(meetingsInfo = if (reset) ResourceState.Refreshing else ResourceState.Loading) }

        meetingsService.fetchMyDailyMeetings(start, end)
            .onSuccess { fetched ->
                _uiState.update { it.copy(meetingsInfo = ResourceState.Success(if (reset) fetched else currentData + fetched)) }
            }
            .onFailure { error ->
                Log.e("ERROR", "ERROR", error)
                _uiState.update { it.copy(meetingsInfo = ResourceState.Error(error)) }
            }
    }

    fun runFetchMeetingsInfo(start: LocalDate, end: LocalDate, reset: Boolean = false) = viewModelScope.launch { fetchMeetingsInfo(start, end, reset) }

    suspend fun fetchMeetingsShort(start: LocalDate, end: LocalDate, reset: Boolean = false) {
        val currentData = (_uiState.value.meetingsInfoShort as? ResourceState.Success)?.data ?: emptyMap()
        _uiState.update { it.copy(meetingsInfoShort = if (reset) ResourceState.Refreshing else ResourceState.Loading) }

        meetingsService.fetchMyDailyMeetingsShort(start, end)
            .onSuccess { fetched ->
                _uiState.update { it.copy(meetingsInfoShort = ResourceState.Success(if (reset) fetched else currentData + fetched)) }
            }
            .onFailure { error ->
                _uiState.update { it.copy(meetingsInfoShort = ResourceState.Error(error)) }
            }
    }

    fun runFetchMeetingsShort(start: LocalDate, end: LocalDate, reset: Boolean = false) = viewModelScope.launch { fetchMeetingsShort(start, end, reset) }

    fun getMeetingsInfoByDate(date: LocalDate): List<MeetingContext> {
        return (_uiState.value.meetingsInfo as? ResourceState.Success)?.data?.get(date) ?: return emptyList()
    }
}
