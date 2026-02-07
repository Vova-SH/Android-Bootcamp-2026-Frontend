package com.example.meet.ui.screens.meetings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meet.data.dto.UserDto
import com.example.meet.data.source.DataLocator.userInfoDataSource
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class CreateMeetingViewModel : ViewModel() {
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _searchResults = MutableLiveData<List<UserDto>>(emptyList())
    val searchResults: LiveData<List<UserDto>> = _searchResults

    private val _selectedParticipants = MutableLiveData<List<UserDto>>(emptyList())
    val selectedParticipants: LiveData<List<UserDto>> = _selectedParticipants

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    //private val userInfoDataSource = DataLocator.userInfoDataSource

    private var allUsersCache: List<UserDto> = emptyList()

    init {
        loadAllUsers()
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                allUsersCache = userInfoDataSource.loadAllUsers()
                _searchResults.value = allUsersCache
            } catch (e: Exception) {
                // Можно добавить обработку ошибок
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query

        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.value = allUsersCache
            } else {
                try {
                    val filteredUsers = userInfoDataSource.searchUsers(query)
                    _searchResults.value = filteredUsers
                } catch (e: Exception) {
                    _searchResults.value = emptyList()
                }
            }
        }
    }

    fun addParticipant(user: UserDto) {
        val current = _selectedParticipants.value ?: emptyList()
        if (!current.any { it.id == user.id }) {
            _selectedParticipants.value = current + user
        }
    }

    fun removeParticipant(user: UserDto) {
        val current = _selectedParticipants.value ?: emptyList()
        _selectedParticipants.value = current.filter { it.id != user.id }
    }

    fun clearSelectedParticipants() {
        _selectedParticipants.value = emptyList()
    }
}