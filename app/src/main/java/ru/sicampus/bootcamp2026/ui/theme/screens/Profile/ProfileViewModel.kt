package ru.sicampus.bootcamp2026.ui.theme.screens.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.dto.ProfileUpdateDTO
import ru.sicampus.bootcamp2026.data.source.ProfileNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import kotlin.String



class ProfileViewModel(
    private val appViewModel: AppViewModel,
    private val userPreferences: UserPreferences

): ViewModel() {
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.NoEdContent("","","",""))

    val uiState = _uiState.asStateFlow()

    init{
        getData()
    }
    fun switchToEditMode(fio: String, jobTitle: String, email: String, password: String) {
        _uiState.value = ProfileState.EdContent(fio, jobTitle, email, password)
    }

    fun cancelChanges(){
        viewModelScope.launch {
            val currentEmail = userPreferences.getUserEmail() ?: return@launch
            val dataSource = UsersInfoDataSource()
            val result = dataSource.getUserByEmail(currentEmail)
            val user = result.getOrNull() ?: return@launch

            _uiState.value = ProfileState.NoEdContent(
                fullName = user.fullName ?: "",
                jobTitle = user.jobTitle ?: "",
                email = user.email ?: "",
                avatarUrl = user.avatarUrl ?: ""
            )
        }
    }

    fun saveChanges(fullName: String, jobTitle: String, email: String,  avatarUrl: String, currentPassword: String, newPassword: String){
        viewModelScope.launch {
            _uiState.value = ProfileState.Loading

            val currentEmail = userPreferences.getUserEmail()

            val dataSource = UsersInfoDataSource()

            val result = dataSource.getUserByEmail(currentEmail)
            val userDTO = result.getOrNull()

            val updateData = ProfileUpdateDTO(
                fullName = fullName,
                jobTitle = jobTitle,
                email = email,
                avatarUrl = avatarUrl,

            )

            val profileDataSource = ProfileNetworkDataSource()

            val success = profileDataSource.updateProfile(
                _userId = userDTO?.id,
                updateData = updateData,
                currentPassword = currentPassword,
                currentEmail = currentEmail
            )

            if (success) {
                _uiState.value = ProfileState.NoEdContent(fullName, jobTitle, email, avatarUrl)
                if (email != currentEmail) {
                    userPreferences.saveUserEmail(email)
                }
            } else {
                // Ошибка
            }

        }
    }


    fun toInvitations() {
        appViewModel.NavigateTo(ViewModelState.Invitations)
    }

    fun toTimeTable() {
        appViewModel.NavigateTo(ViewModelState.TimeTable)
    }




    fun getData() {
        viewModelScope.launch {
            val dataSource = UsersInfoDataSource()
            val myEmail = userPreferences.getUserEmail() ?: "мой@email.com"

            val result = dataSource.getUserByEmail(myEmail)
            val userDTO = result.getOrNull()

            _uiState.emit(ProfileState.NoEdContent(
                fullName = userDTO?.fullName ?: "Имя не указано",
                jobTitle = userDTO?.jobTitle ?: "Без должности",
                email = userDTO?.email ?: myEmail,
                avatarUrl = userDTO?.avatarUrl ?: "None",

            ))


            delay(timeMillis = 2000L)

        }
    }
}