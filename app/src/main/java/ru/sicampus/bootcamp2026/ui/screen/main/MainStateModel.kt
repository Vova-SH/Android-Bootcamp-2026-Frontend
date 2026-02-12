package ru.sicampus.bootcamp2026.ui.screen.main

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.CreateMeetingUseCase
import ru.sicampus.bootcamp2026.domain.GetCurrentUserUseCase
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.util.ErrorUtils
import java.time.LocalDate

@SuppressLint("NewApi")
class MainStateModel(
    private val getUsersUseCase: GetUsersUseCase = GetUsersUseCase(),
    private val getCurrentUserUseCase: GetCurrentUserUseCase = GetCurrentUserUseCase(),
    private val createMeetingUseCase: CreateMeetingUseCase = CreateMeetingUseCase()
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun onMeetingNameChange(name: String) {
        _uiState.update { it.copy(meetingName = name) }
    }

    fun onMeetingDateChange(date: String) {
        _uiState.update { it.copy(meetingDate = date) }
    }

    fun onMeetingTimeChange(time: String) {
        _uiState.update { it.copy(meetingTime = time) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchUsers(query)
    }

    fun onUserSelect(user: UserEntity) {
        _uiState.update { state ->
            if (state.selectedUsers.any { it.id == user.id } || user.id == state.currentUser?.id) {
                state
            } else {
                state.copy(
                    selectedUsers = state.selectedUsers + user,
                    searchQuery = "",
                    foundUsers = emptyList()
                )
            }
        }
    }

    fun onUserRemove(userId: Long) {
        _uiState.update { state ->
            state.copy(selectedUsers = state.selectedUsers.filter { it.id != userId })
        }
    }

    private fun searchUsers(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _uiState.update { it.copy(foundUsers = emptyList()) }
                return@launch
            }
            
            getUsersUseCase(page = 0, query = query).onSuccess { users ->
                _uiState.update { state ->
                    val filteredUsers = users.filter { user ->
                        state.selectedUsers.none { it.id == user.id } && user.id != state.currentUser?.id
                    }
                    state.copy(foundUsers = filteredUsers)
                }
            }.onFailure { e ->
                _uiState.update { it.copy(error = ErrorUtils.translateError(e, "Ошибка при поиске пользователей")) }
            }
        }
    }

    fun onCreateMeetingClick() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.meetingName.isBlank() || state.meetingDate.isBlank() || state.meetingTime.isBlank()) {
                _uiState.update { it.copy(error = "Пожалуйста, заполните все поля") }
                return@launch
            }

            val currentUser = state.currentUser ?: return@launch
            
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val date = LocalDate.parse(state.meetingDate)
                val time = state.meetingTime.split(":")[0].toInt()
                
                createMeetingUseCase(
                    title = state.meetingName,
                    organizerId = currentUser.id,
                    date = date,
                    startHour = time,
                    inviteeIds = state.selectedUsers.map { it.id }
                ).onSuccess {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            isSuccess = true,
                            meetingName = "",
                            meetingDate = "",
                            meetingTime = "",
                            selectedUsers = emptyList()
                        ) 
                    }
                }.onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = ErrorUtils.translateError(e, "Ошибка при создании встречи")) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Неверный формат даты или времени") }
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val currentUserResult = getCurrentUserUseCase()
            
            currentUserResult.onSuccess { user ->
                _uiState.update { it.copy(currentUser = user) }
            }.onFailure { e ->
                _uiState.update { it.copy(error = "Ошибка при загрузке данных пользователя: ${ErrorUtils.translateError(e, "")}") }
            }

            getUsersUseCase(page = 0).onSuccess { users ->
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = ErrorUtils.translateError(e, "Ошибка при загрузке пользователей")) }
            }
        }
    }
}
