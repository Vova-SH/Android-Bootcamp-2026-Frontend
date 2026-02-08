package ru.sicampus.bootcamp2026.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.SessionManager
import ru.sicampus.bootcamp2026.data.source.TokenStorage
import ru.sicampus.bootcamp2026.domain.usecase.DeleteAccountUseCase
import ru.sicampus.bootcamp2026.domain.usecase.GetUserProfileUseCase
import ru.sicampus.bootcamp2026.domain.usecase.LogoutUseCase
import ru.sicampus.bootcamp2026.domain.usecase.UpdateUserProfileUseCase

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<UserDto?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent = _logoutEvent.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            getUserProfileUseCase()
                .onSuccess { _user.value = it }
                .onFailure { _message.value = "Ошибка загрузки профиля: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun updateProfile(firstName: String, secondName: String, position: String) {
        viewModelScope.launch {
            _isLoading.value = true
            updateUserProfileUseCase(firstName, secondName, position)
                .onSuccess {
                    _user.value = it
                    _message.value = "Профиль обновлен"
                }
                .onFailure { _message.value = "Ошибка обновления: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase.invoke()
            } catch (e: Exception) {
            } finally {
                TokenStorage.accessToken = null
                TokenStorage.userId = null
                SessionManager.clear()

                _logoutEvent.value = true
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _isLoading.value = true
            deleteAccountUseCase()
                .onSuccess {
                    TokenStorage.accessToken = null
                    TokenStorage.userId = null
                    SessionManager.clear()
                    _logoutEvent.value = true
                }
                .onFailure { _message.value = "Ошибка удаления аккаунта: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun clearMessage() { _message.value = null }
}