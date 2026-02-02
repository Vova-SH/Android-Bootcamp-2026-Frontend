package ru.sicampus.bootcamp2026.ui.screen.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.repository.MeetingRepositoryImpl
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class MeetingsViewModel : ViewModel() {
    private val repository: MeetingRepository = MeetingRepositoryImpl()

    private val _meetings = MutableStateFlow<List<Meeting>>(emptyList())
    val meetings: StateFlow<List<Meeting>> = _meetings.asStateFlow()

    init {
        loadMeetings()
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            _meetings.value = repository.getMeetings()
        }
    }
}