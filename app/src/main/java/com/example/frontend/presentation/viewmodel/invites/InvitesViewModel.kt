package com.example.frontend.presentation.viewmodel.invites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.auth.SessionManager
import com.example.frontend.data.model.InviteDTO
import com.example.frontend.data.model.InviteWithMeetupDTO
import com.example.frontend.data.model.PersonShortDTO
import com.example.frontend.data.repository.MeetupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class InvitesState(
    val isLoading: Boolean = false,
    val invitesList: List<InviteWithMeetupDTO> = emptyList(),
    val errorMsg: String? = null,
    val responseSuccess: Boolean = false
)

class InvitesViewModel : ViewModel() {
    
    private val repo = MeetupRepository()
    private var sm: SessionManager? = null
    
    private val s = MutableStateFlow(InvitesState())
    val uiState: StateFlow<InvitesState> = s
    
    fun setSessionManager(m: SessionManager) {
        sm = m
        val uid = m.getUserId()
        if (uid > 0) {
            repo.setCurrentUserId(uid)
        }
    }
    
    fun loadUserInvites() {
        viewModelScope.launch {
            s.value = s.value.copy(isLoading = true, errorMsg = null)
            
            try {
                val m = sm
                if (m == null) {
                    s.value = s.value.copy(
                        isLoading = false,
                        errorMsg = "Session manager not initialized"
                    )
                    return@launch
                }
                
                val uid = m.getUserId()
                if (uid <= 0) {
                    s.value = s.value.copy(
                        isLoading = false,
                        errorMsg = "Пользователь не авторизован"
                    )
                    return@launch
                }
                
                repo.setCurrentUserId(uid)
                val u = repo.getCurrentPerson()
                val invs = u.invites ?: emptyList()
                
                s.value = s.value.copy(isLoading = false, invitesList = invs)
            } catch (e: Exception) {
                s.value = s.value.copy(
                    isLoading = false,
                    errorMsg = e.message ?: "Ошибка загрузки приглашений"
                )
            }
        }
    }
    
    // обновляет статус приглашения
    fun respondToInvitation(invid: Long, ok: Boolean) {
        viewModelScope.launch {
            s.value = s.value.copy(isLoading = true, errorMsg = null)
            
            try {
                val invs = s.value.invitesList
                val inv = invs.find { it.id == invid }
                
                if (inv == null) {
                    s.value = s.value.copy(
                        isLoading = false,
                        errorMsg = "Приглашение не найдено"
                    )
                    return@launch
                }
                
                val u = repo.getCurrentPerson()
                val p = PersonShortDTO(
                    id = u.id,
                    name = u.name,
                    login = u.login,
                    photo = u.photo,
                    dept = u.dept
                )
                
                val upd = InviteDTO(
                    id = invid,
                    agree = ok,
                    meetup = inv.meetup,
                    participant = p
                )
                
                repo.updateInvite(invid, upd)
                
                s.value = s.value.copy(isLoading = false, responseSuccess = true)
                
                loadUserInvites()
            } catch (e: Exception) {
                s.value = s.value.copy(
                    isLoading = false,
                    errorMsg = e.message ?: "Ошибка ответа на приглашение"
                )
            }
        }
    }
    
    fun clearResponseSuccess() {
        s.value = s.value.copy(responseSuccess = false)
    }
    
    fun clearError() {
        s.value = s.value.copy(errorMsg = null)
    }
}
