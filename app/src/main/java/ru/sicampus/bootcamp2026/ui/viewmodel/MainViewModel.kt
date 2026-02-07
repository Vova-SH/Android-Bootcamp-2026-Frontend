package ru.sicampus.bootcamp2026.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.auth.NetworkClient
import ru.sicampus.bootcamp2026.data.auth.TokenStorage
import ru.sicampus.bootcamp2026.data.model.MeetingDto
import ru.sicampus.bootcamp2026.data.model.SharedEvents
import ru.sicampus.bootcamp2026.data.repository.AppRepository
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val tokenStorage = TokenStorage(app)
    private val api = NetworkClient.createAppApi()
    private val repository = AppRepository(api, tokenStorage)

    private val _allMeetings = MutableStateFlow<List<MeetingDto>>(emptyList())

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    val meetings = combine(_allMeetings, _selectedDate) { list, date ->
        list.filter { meeting ->
            try {
                val meetingDate = OffsetDateTime.parse(meeting.startsAt).toLocalDate()
                meetingDate == date
            } catch (e: Exception) {
                false
            }
        }
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMeetings()

        val stomp = NetworkClient.getStompManager(tokenStorage)
        stomp.connect()

        viewModelScope.launch {
            stomp.updates.collect { loadMeetings() }
        }
        viewModelScope.launch {
            SharedEvents.meetingsUpdated.collect { loadMeetings() }
        }
    }

    fun loadMeetings() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getMeetings()
                .onSuccess { data -> _allMeetings.value = data }
                .onFailure { Log.e("MainViewModel", "Error", it) }
            _isLoading.value = false
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
}