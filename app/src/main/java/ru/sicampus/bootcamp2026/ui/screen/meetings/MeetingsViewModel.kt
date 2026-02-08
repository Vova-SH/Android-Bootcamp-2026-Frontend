// MeetingsViewModel.kt - Обновленная версия
package ru.sicampus.bootcamp2026.ui.screen.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.network.MeetingRepository
import ru.sicampus.bootcamp2026.data.network.UserRepository
import ru.sicampus.bootcamp2026.data.network.source.MeetingDataSource
import ru.sicampus.bootcamp2026.data.network.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetMeetingsForMe
import ru.sicampus.bootcamp2026.domain.GetMeetingsOutMe
import ru.sicampus.bootcamp2026.ui.screen.users.UsersScreenState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek

/**
 * ViewModel для управления списком встреч с серверным поиском
 */
class MeetingsViewModel : ViewModel() {
    private val getMeetingsForMe = GetMeetingsForMe(
        meetingRepository = MeetingRepository(MeetingDataSource())
    )
    private val getMeetingsOutMe = GetMeetingsOutMe(
        meetingRepository = MeetingRepository(MeetingDataSource())
    )
    private val _uiState: MutableStateFlow<MeetingsState> = MutableStateFlow(MeetingsState.Loading)
    val uiState = _uiState.asStateFlow()


    init {
        getDataForMe()
    }


    fun getDataForMe() {
        viewModelScope.launch {
            _uiState.emit(MeetingsState.Loading)
            getMeetingsForMe.invoke().fold(
                onSuccess = { data ->
                    _uiState.emit(MeetingsState.Content(data))
                },
                onFailure = { error ->
                    _uiState.emit(MeetingsState.Error(error.message.orEmpty()) { getDataForMe() })
                }
            )
        }

    }
    fun getDataOutMe() {
        viewModelScope.launch {
            _uiState.emit(MeetingsState.Loading)
            getMeetingsOutMe.invoke().fold(
                onSuccess = { data ->
                    _uiState.emit(MeetingsState.Content(data))
                },
                onFailure = { error ->
                    _uiState.emit(MeetingsState.Error(error.message.orEmpty()) { getDataOutMe() })
                }
            )
        }
    }
}