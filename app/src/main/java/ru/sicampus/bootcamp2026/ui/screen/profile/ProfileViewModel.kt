package ru.sicampus.bootcamp2026.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.usecase.GetUserProfileUseCase
import ru.sicampus.bootcamp2026.domain.usecase.UpdateUserProfileUseCase

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<UserDto?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            getUserProfileUseCase()
                .onSuccess { _user.value = it }
                .onFailure { _message.value = "Error loading profile: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun updateProfile(firstName: String, secondName: String, position: String) {
        viewModelScope.launch {
            _isLoading.value = true
            updateUserProfileUseCase(firstName, secondName, position)
                .onSuccess {
                    _user.value = it
                    _message.value = "Profile updated successfully"
                }
                .onFailure { _message.value = "Error updating profile: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun clearMessage() { _message.value = null }
}