package com.example.frontend.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.auth.SessionManager
import com.example.frontend.data.model.MeetupDTO
import com.example.frontend.data.model.PersonShortDTO
import com.example.frontend.data.model.PersonWithInvitesDTO
import com.example.frontend.data.repository.MeetupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val isLoading: Boolean = false,
    val userInfo: PersonWithInvitesDTO? = null,
    val organizedMeetups: List<MeetupDTO> = emptyList(),
    val errorMsg: String? = null,
    val updateSuccess: Boolean = false
)

class ProfileViewModel : ViewModel() {
    
    private val repo = MeetupRepository()
    private var sm: SessionManager? = null
    
    private val s = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = s
    
    fun setSessionManager(m: SessionManager) {
        sm = m
        val uid = m.getUserId()
        if (uid > 0) {
            repo.setCurrentUserId(uid)
        }
    }
    
    // загружает профиль пользователя
    fun loadUserProfile() {
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
                val ms = u.meetups ?: emptyList()
                
                s.value = s.value.copy(isLoading = false, userInfo = u, organizedMeetups = ms)
            } catch (e: Exception) {
                s.value = s.value.copy(
                    isLoading = false,
                    errorMsg = e.message ?: "Ошибка загрузки профиля"
                )
            }
        }
    }
    
    // обновляет имя и отдел
    fun updateProfile(uid: Long, name: String, dept: String) {
        viewModelScope.launch {
            s.value = s.value.copy(isLoading = true, errorMsg = null)
            
            val cur = s.value.userInfo
            if (cur == null) {
                s.value = s.value.copy(
                    isLoading = false,
                    errorMsg = "Нет данных пользователя"
                )
                return@launch
            }
            
            try {
                val upd = PersonShortDTO(
                    id = uid,
                    name = name,
                    login = cur.login,
                    photo = cur.photo,
                    dept = dept
                )
                
                val r = repo.updatePerson(uid, upd)
                
                s.value = s.value.copy(
                    isLoading = false,
                    userInfo = r,
                    updateSuccess = true,
                    errorMsg = null
                )
            } catch (e: Exception) {
                s.value = s.value.copy(
                    isLoading = false,
                    errorMsg = e.message ?: "Ошибка обновления профиля",
                    updateSuccess = false
                )
            }
        }
    }
    
    fun clearError() {
        s.value = s.value.copy(errorMsg = null)
    }
    
    fun clearUpdateSuccess() {
        s.value = s.value.copy(updateSuccess = false)
    }
}
