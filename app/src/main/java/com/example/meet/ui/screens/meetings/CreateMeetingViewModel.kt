package com.example.meet.ui.screens.meetings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meet.data.dto.UserDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateMeetingViewModel : ViewModel() {
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _searchResults = MutableLiveData<List<UserDto>>(emptyList())
    val searchResults: LiveData<List<UserDto>> = _searchResults

    private val _selectedParticipants = MutableLiveData<List<UserDto>>(emptyList())
    val selectedParticipants: LiveData<List<UserDto>> = _selectedParticipants

    private val _allUsers = MutableLiveData<List<UserDto>>(emptyList())
    val allUsers: LiveData<List<UserDto>> = _allUsers

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchUsers(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        val filtered = _allUsers.value?.filter { user ->
            user.fullName.contains(query, ignoreCase = true) ||
                    user.email.contains(query, ignoreCase = true) ||
                    user.position?.contains(query, ignoreCase = true) == true ||
                    user.department?.contains(query, ignoreCase = true) == true
        } ?: emptyList()

        _searchResults.value = filtered
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    fun setAllUsers(users: List<UserDto>) {
        _allUsers.value = users
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
}