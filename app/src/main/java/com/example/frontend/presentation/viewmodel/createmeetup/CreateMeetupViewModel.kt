package com.example.frontend.presentation.viewmodel.createmeetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.MeetupDTO
import com.example.frontend.data.model.MeetupToCreateDTO
import com.example.frontend.data.model.PersonWithInvitesDTO
import com.example.frontend.data.model.InviteCreateDTO
import com.example.frontend.data.repository.MeetupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CreateMeetupState(
    val isLoading: Boolean = false,
    val availableUsers: List<PersonWithInvitesDTO> = emptyList(),
    val selectedUserIds: Set<Long> = emptySet(),
    val createdMeetup: MeetupDTO? = null,
    val errorMessage: String? = null
)

class CreateMeetupViewModel : ViewModel() {
    
    private val repo = MeetupRepository()
    
    private val s = MutableStateFlow(CreateMeetupState())
    val uiState: StateFlow<CreateMeetupState> = s
    
    init {
        loadUsers()
    }
    
    private fun loadUsers() {
        viewModelScope.launch {
            s.value = s.value.copy(isLoading = true)
            
            try {
                val u = repo.getAllPersons()
                s.value = s.value.copy(isLoading = false, availableUsers = u)
            } catch (e: Exception) {
                s.value = s.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error loading users"
                )
            }
        }
    }
    
    fun toggleUserSelection(uid: Long) {
        val sel = s.value.selectedUserIds.toMutableSet()
        if (sel.contains(uid)) {
            sel.remove(uid)
        } else {
            sel.add(uid)
        }
        s.value = s.value.copy(selectedUserIds = sel)
    }
    
    // создает митап и отправляет приглашения
    fun createMeetupWithInvites(date: String, time: String, pid: Long) {
        viewModelScope.launch {
            s.value = s.value.copy(isLoading = true, errorMessage = null)
            
            try {
                val md = MeetupToCreateDTO(date = date, time = time, plannerId = pid)
                val m = repo.createMeetup(md)
                
                val sel = s.value.selectedUserIds
                for (uid in sel) {
                    try {
                        val inv = InviteCreateDTO(
                            meetupId = m.id,
                            participantId = uid,
                            agree = false
                        )
                        repo.createInvite(inv)
                    } catch (e: Exception) {
                    }
                }
                
                s.value = s.value.copy(isLoading = false, createdMeetup = m, errorMessage = null)
            } catch (e: Exception) {
                s.value = s.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error creating meetup"
                )
            }
        }
    }
    
    fun clearCreatedMeetup() {
        s.value = s.value.copy(createdMeetup = null)
    }
    
    fun clearError() {
        s.value = s.value.copy(errorMessage = null)
    }
}
