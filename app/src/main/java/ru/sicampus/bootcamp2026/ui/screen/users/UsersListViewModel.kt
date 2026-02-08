package ru.sicampus.bootcamp2026.ui.screen.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.usecase.GetUsersUseCase

class UsersListViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private var allUsersCache: List<UserDto> = emptyList()

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getUsersUseCase()
                .onSuccess { result ->
                    allUsersCache = result
                    _users.value = result
                }
                .onFailure {
                    _error.value = "Не удалось загрузить список: ${it.message}"
                }

            _isLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) {
            _users.value = allUsersCache
        } else {
            _users.value = allUsersCache.filter { user ->
                user.firstName.contains(trimmedQuery, ignoreCase = true) ||
                        user.secondName.contains(trimmedQuery, ignoreCase = true) ||
                        user.email.contains(trimmedQuery, ignoreCase = true)
            }
        }
    }
}