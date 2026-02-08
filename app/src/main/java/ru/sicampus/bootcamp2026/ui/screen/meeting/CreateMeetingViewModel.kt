package ru.sicampus.bootcamp2026.screen.meetings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CreateMeetingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<CreateMeetingState>(CreateMeetingState())
    val uiState: StateFlow<CreateMeetingState> = _uiState.asStateFlow()

    private val repository = CreateMeetingRepository()

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onDateChange(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    fun onTimeChange(time: String) {
        _uiState.value = _uiState.value.copy(selectedTime = time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCreateMeeting() {
        val state = _uiState.value
        if (state.name.isBlank() || state.selectedDate.isBlank() || state.selectedTime.isBlank()) {
            _uiState.value = state.copy(error = "Заполните все поля")
            return
        }

        val dateTimeString = "${state.selectedDate}T${state.selectedTime}"
        val startDateTime = try {
            LocalDateTime.parse(dateTimeString)
        } catch (e: Exception) {
            _uiState.value = state.copy(error = "Неверный формат даты/времени")
            return
        }

        val endDateTime = startDateTime.plusHours(1)

        viewModelScope.launch {
            repository.createMeeting(
                name = state.name,
                start_time = startDateTime,
                end_time = endDateTime
            ).fold(
                onSuccess = {
                    _uiState.value = state.copy(success = true, error = null)
                },
                onFailure = { throwable ->
                    _uiState.value = state.copy(error = throwable.message
                        ?: "Ошибка создания встречи")
                }
            )
        }
    }
}

data class CreateMeetingState(
    val name: String = "",
    val selectedDate: String = "",
    val selectedTime: String = "",
    val error: String? = null,
    val success: Boolean = false
)