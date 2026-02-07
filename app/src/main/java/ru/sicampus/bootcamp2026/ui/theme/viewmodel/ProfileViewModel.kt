package ru.sicampus.bootcamp2026.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.ui.theme.state.UserProfileState

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserProfileState>(UserProfileState.Loading)
    val uiState: StateFlow<UserProfileState> = _uiState

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = UserProfileState.Loading

            try {
                val userDto = userRepository.getUser
                _uiState.value = UserProfileState.Success(userDto as UserDto)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Неизвестная ошибка"
                _uiState.value = UserProfileState.Error(message = errorMsg)
            }
        }
    }
}