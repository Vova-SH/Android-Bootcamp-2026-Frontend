package ru.sicampus.bootcamp2026.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.MeetingRepository
import ru.sicampus.bootcamp2026.data.model.MeetingDto

class MainViewModel : ViewModel() {

    private val repository = MeetingRepository()

    private val _meetings = MutableStateFlow<List<MeetingDto>>(emptyList())
    val meetings: StateFlow<List<MeetingDto>> = _meetings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMeetings()
    }

    private fun loadMeetings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = repository.getMeetings()
                _meetings.value = data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}