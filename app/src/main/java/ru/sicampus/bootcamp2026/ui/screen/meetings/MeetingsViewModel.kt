package ru.sicampus.bootcamp2026.ui.screen.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.usecase.CreateMeetingUseCase
import ru.sicampus.bootcamp2026.domain.usecase.DeleteMeetingUseCase
import ru.sicampus.bootcamp2026.domain.usecase.GetMeetingScheduleUseCase
import ru.sicampus.bootcamp2026.domain.usecase.GetMeetingsUseCase

class MeetingsViewModel(
    private val getMeetingsUseCase: GetMeetingsUseCase,
    private val getMeetingScheduleUseCase: GetMeetingScheduleUseCase,
    private val createMeetingUseCase: CreateMeetingUseCase,
    private val deleteMeetingUseCase: DeleteMeetingUseCase
) : ViewModel() {

    private val _meetings = MutableStateFlow<List<Meeting>>(emptyList())
    val meetings = _meetings.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isScheduleMode = MutableStateFlow(false)
    val isScheduleMode = _isScheduleMode.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false
    private val pageSize = 10

    init {
        loadData(reset = true)
    }

    fun toggleMode() {
        _isScheduleMode.value = !_isScheduleMode.value
        loadData(reset = true)
    }

    fun loadData(reset: Boolean = false) {
        if (_isScheduleMode.value) {
            loadSchedule()
        } else {
            loadPaginatedMeetings(reset)
        }
    }

    private fun loadSchedule() {
        viewModelScope.launch {
            _isLoading.value = true
            getMeetingScheduleUseCase()
                .onSuccess { _meetings.value = it }
                .onFailure { _error.value = "Ошибка загрузки расписания: ${it.message}" }
            _isLoading.value = false
        }
    }

    private fun loadPaginatedMeetings(reset: Boolean) {
        if (isLoading.value || (isLastPage && !reset)) return

        if (reset) {
            currentPage = 0
            isLastPage = false
            _meetings.value = emptyList()
        }

        viewModelScope.launch {
            _isLoading.value = true
            getMeetingsUseCase(currentPage, pageSize)
                .onSuccess { newItems ->
                    if (newItems.isEmpty()) {
                        isLastPage = true
                    } else {
                        _meetings.value += newItems
                        if (newItems.size < pageSize) isLastPage = true
                        else currentPage++
                    }
                }
                .onFailure { _error.value = "Не удалось загрузить встречи: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun deleteMeeting(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            deleteMeetingUseCase(id)
                .onSuccess {
                    _meetings.value = _meetings.value.filter { it.id != id }
                }
                .onFailure { _error.value = "Не удалось удалить: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun createMeeting(title: String, desc: String, place: String, date: String, duration: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            createMeetingUseCase(title, desc, place, date, duration)
                .onSuccess { loadData(reset = true) }
                .onFailure { _error.value = "Не удалось создать: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun clearError() { _error.value = null }
}