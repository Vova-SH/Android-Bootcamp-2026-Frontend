package com.example.frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.*
import com.example.frontend.data.repository.MeetupRepository
import com.example.frontend.data.auth.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: Long, val userName: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class MeetupViewModel : ViewModel() {
    
    private val repo = MeetupRepository()
    private var sm: SessionManager? = null
    
    fun setSessionManager(m: SessionManager) {
        sm = m
    }

    private val ms = MutableStateFlow<UiState<List<MeetupWithInvitesDTO>>>(UiState.Idle)
    val meetupsState: StateFlow<UiState<List<MeetupWithInvitesDTO>>> = ms

    private val ps = MutableStateFlow<UiState<List<PersonWithInvitesDTO>>>(UiState.Idle)
    val personsState: StateFlow<UiState<List<PersonWithInvitesDTO>>> = ps

    private val cps = MutableStateFlow<UiState<PersonWithInvitesDTO>>(UiState.Idle)
    val currentPersonState: StateFlow<UiState<PersonWithInvitesDTO>> = cps

    private val ds = MutableStateFlow<UiState<List<DepartmentDTO>>>(UiState.Idle)
    val departmentsState: StateFlow<UiState<List<DepartmentDTO>>> = ds

    private val cms = MutableStateFlow<UiState<MeetupDTO>>(UiState.Idle)
    val createMeetupState: StateFlow<UiState<MeetupDTO>> = cms

    private val aus = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = aus

    fun loadAllMeetups() {
        viewModelScope.launch {
            ms.value = UiState.Loading
            try {
                val m = repo.getAllMeetups()
                ms.value = UiState.Success(m)
            } catch (e: Exception) {
                ms.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // загружает митапы конкретного юзера
    fun loadAllPersonsMeetups() {
        viewModelScope.launch {
            ms.value = UiState.Loading
            try {
                val s = sm ?: throw Exception("User not authenticated")
                val m = repo.getAllCurrentPersonsMeetups(s.getUserId())
                ms.value = UiState.Success(m)
            } catch (e: Exception) {
                ms.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun createMeetup(m: MeetupToCreateDTO) {
        viewModelScope.launch {
            cms.value = UiState.Loading
            try {
                val c = repo.createMeetup(m)
                cms.value = UiState.Success(c)
                loadAllMeetups()
            } catch (e: Exception) {
                cms.value = UiState.Error(e.message ?: "Failed to create meetup")
            }
        }
    }
    
    fun deleteMeetup(id: Long) {
        viewModelScope.launch {
            try {
                repo.deleteMeetup(id)
                loadAllMeetups()
            } catch (e: Exception) {
                ms.value = UiState.Error(e.message ?: "Failed to delete meetup")
            }
        }
    }

    fun loadAllPersons() {
        viewModelScope.launch {
            ps.value = UiState.Loading
            try {
                val p = repo.getAllPersons()
                ps.value = UiState.Success(p)
            } catch (e: Exception) {
                ps.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun loadPersonById(id: Long) {
        viewModelScope.launch {
            cps.value = UiState.Loading
            try {
                val p = repo.getPersonById(id)
                cps.value = UiState.Success(p)
            } catch (e: Exception) {
                cps.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun registerPerson(p: PersonRegisterDTO) {
        viewModelScope.launch {
            try {
                repo.registerPerson(p)
                loadAllPersons()
            } catch (e: Exception) {
                ps.value = UiState.Error(e.message ?: "Failed to register person")
            }
        }
    }

    fun loadAllDepartments() {
        viewModelScope.launch {
            ds.value = UiState.Loading
            try {
                val d = repo.getAllDepartments()
                ds.value = UiState.Success(d)
            } catch (e: Exception) {
                ds.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateInviteResponse(id: Long, agree: Boolean, inv: InviteDTO) {
        viewModelScope.launch {
            try {
                val u = inv.copy(agree = agree)
                repo.updateInvite(id, u)
                loadAllMeetups()
            } catch (e: Exception) {
                ms.value = UiState.Error(e.message ?: "Failed to update invite")
            }
        }
    }
    
    fun resetCreateMeetupState() {
        cms.value = UiState.Idle
    }

    // авторизует пользователя
    fun login(login: String, pass: String) {
        viewModelScope.launch {
            aus.value = AuthState.Loading
            try {
                val p = repo.login()

                if (p == null) {
                    aus.value = AuthState.Error("Сервер не вернул данные пользователя")
                    return@launch
                }
                
                sm?.let { s ->
                    s.saveCredentials(login, pass)
                    s.saveUserId(p.id)
                    s.saveUserName(p.name)
                }
                
                aus.value = AuthState.Success(userId = p.id, userName = p.name)
            } catch (e: Exception) {
                aus.value = AuthState.Error(e.message ?: "Auth Error")
            }
        }
    }
    
    fun logout() {
        sm?.clearAll()
        aus.value = AuthState.Idle
    }
    
    fun checkAuthStatus(): Boolean {
        return sm?.isLoggedIn() ?: false
    }
}
