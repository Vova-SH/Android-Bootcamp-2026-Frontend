package ru.sicampus.bootcamp2026.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.auth.NetworkClient
import ru.sicampus.bootcamp2026.data.auth.TokenStorage
import ru.sicampus.bootcamp2026.data.model.UserDto
import ru.sicampus.bootcamp2026.data.repository.AppRepository

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val tokenStorage = TokenStorage(app)
    private val api = NetworkClient.createAppApi()
    private val repository = AppRepository(api, tokenStorage)

    private val _user = MutableStateFlow<UserDto?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getCurrentUser()
                .onSuccess { _user.value = it }
                .onFailure { showToast("Ошибка: ${it.message}") }
            _isLoading.value = false
        }
    }

    fun updateField(
        name: String? = null,
        position: String? = null,
        email: String? = null,
        phone: String? = null,
        birthDate: String? = null
    ) {
        val current = _user.value ?: return

        val newName = name ?: current.name ?: ""
        val newPosition = position ?: current.position ?: ""
        val newEmail = email ?: current.email ?: ""

        val newPhone = phone ?: current.phone
        val newBirthDate = birthDate ?: current.birthDate

        viewModelScope.launch {
            _isLoading.value = true
            repository.updateUser(current.id, newName, newPosition, newEmail, newPhone, newBirthDate)
                .onSuccess {
                    _user.value = it
                    showToast("Сохранено")
                }
                .onFailure { showToast("Ошибка: ${it.message}") }
            _isLoading.value = false
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}