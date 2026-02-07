package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.data.source.MeetingCreateNetDataSource
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MeetingInfoViewModel(private val appViewModel: AppViewModel): ViewModel() {
    private val _uiState: MutableStateFlow<MeetingInfoState> = MutableStateFlow(MeetingInfoState.Loading)

    val uiState = _uiState.asStateFlow()
    val getUsersUseCase = GetUsersUseCase(UserRepository(UsersInfoDataSource()))
    init {
        getData()
    }




    @SuppressLint("NewApi")
    fun convertToISO(dateStr: String, timeStr: String): String? {
        return try{
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val localDateTime = LocalDateTime.parse("$dateStr $timeStr", formatter)
            localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".000Z"
        }
        catch (e: Exception) {
            null
        }
    }


    fun getData(){
        viewModelScope.launch {
            _uiState.emit(MeetingInfoState.Loading)
            getUsersUseCase.invoke(0).fold(
                onSuccess = {data ->
                    _uiState.emit(MeetingInfoState.Content(users = data))
                },
                onFailure = {error ->
                    _uiState.emit(MeetingInfoState.Error(error.message.orEmpty()))
                }

            )
        }
    }
}