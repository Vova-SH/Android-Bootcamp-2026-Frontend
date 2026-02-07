package ru.sicampus.bootcamp2026.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.auth.NetworkClient
import ru.sicampus.bootcamp2026.data.auth.TokenStorage
import ru.sicampus.bootcamp2026.data.camera.ImageRepository
import ru.sicampus.bootcamp2026.data.model.UserDto
import ru.sicampus.bootcamp2026.data.repository.AppRepository

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val tokenStorage = TokenStorage(app)
    private val api = NetworkClient.createAppApi()
    private val repository = AppRepository(api, tokenStorage)
    private val imageRepository = ImageRepository()

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
        val currentAvatarUrl = current.avatarUrl

        viewModelScope.launch {
            _isLoading.value = true
            repository.updateUser(current.id, newName, newPosition, newEmail, newPhone, newBirthDate, currentAvatarUrl)
                .onSuccess {
                    _user.value = it
                    showToast("Сохранено")
                }
                .onFailure { showToast("Ошибка: ${it.message}") }
            _isLoading.value = false
        }
    }

    fun uploadAvatar(context: Context, bitmap: Bitmap?, uri: Uri?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val uploadedUrl = when {
                    uri != null -> imageRepository.uploadImageUri(context, uri)
                    bitmap != null -> imageRepository.uploadImage(bitmap)
                    else -> null
                }

                if (uploadedUrl != null) {
                    val current = _user.value
                    if (current != null) {
                        updateUserOnBackend(
                            name = current.name ?: "",
                            position = current.position ?: "",
                            email = current.email ?: "",
                            phone = current.phone,
                            birthDate = current.birthDate,
                            avatarUrl = uploadedUrl
                        )
                    }
                } else {
                    showToast("Не удалось получить фото")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Ошибка загрузки фото: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun updateUserOnBackend(
        name: String,
        position: String,
        email: String,
        phone: String?,
        birthDate: String?,
        avatarUrl: String?
    ) {
        val currentId = _user.value?.id ?: return
        repository.updateUser(currentId, name, position, email, phone, birthDate, avatarUrl)
            .onSuccess {
                _user.value = it
                showToast("Профиль обновлен")
            }
            .onFailure { showToast("Ошибка сохранения: ${it.message}") }
    }

    private fun showToast(msg: String) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}