package ru.sicampus.bootcamp2026.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.domain.usecase.user.GetUserByIdUseCase
import ru.sicampus.bootcamp2026.domain.usecase.user.SearchUserUseCase
import ru.sicampus.bootcamp2026.domain.usecase.user.UserUpdateUseCase
import ru.sicampus.bootcamp2026.utils.SettingsUtils


class ProfileViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUseCase: UserUpdateUseCase,
    private val searchUserUserCase: SearchUserUseCase
) : ViewModel() {
    private val settingsUtils = SettingsUtils(App.context)

    private val _uiState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private val _state = MutableStateFlow(UserProfileUiData())
    val state: StateFlow<UserProfileUiData> = _state.asStateFlow()

    private val _navigationEvents: Channel<ActionState> = Channel()
    val navigationEvents: Flow<ActionState> = _navigationEvents.receiveAsFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {

        _uiState.update { ProfileState.Loading }

        viewModelScope.launch {
            val result = getUserByIdUseCase(settingsUtils.getUserId())

            result.onSuccess { user ->
                _state.update { state ->
                    state.copy(
                        fullName = user.firstName + " " + user.secondName,
                        email = user.email,
                        position = user.position,
                        department = user.department,
                        description = user.description,
                        photoUrl = user.photoUrl
                    )
                }
                _uiState.update { ProfileState.Data }
            }.onFailure { error ->
                _state.update { state ->
                    state.copy(
                        errorMessage = error.message ?: "Ошибка загрузки данных"
                    )
                }
                _uiState.update { ProfileState.Error }
            }
        }
    }

    private fun updateUser() {
        _uiState.update { ProfileState.EditData }

        viewModelScope.launch {
            val result = updateUseCase(
                settingsUtils.getUserId(),
                _state.value.updateFirstName,
                _state.value.updateSecondName,
                _state.value.updateDescription,
                _state.value.updatePosition,
                _state.value.updateDepartment,
            )

            result.onSuccess { user ->
                _state.update { state ->
                    state.copy(
                        fullName = user.firstName + " " + user.secondName,
                        email = user.email,
                        position = user.position,
                        department = user.department,
                        description = user.description,
                        photoUrl = user.photoUrl
                    )
                }
                _uiState.update { ProfileState.Data }
            }.onFailure { error ->
                _state.update { state ->
                    state.copy(
                        errorMessage = error.message ?: "Ошибка обновления"
                    )
                }
                _uiState.update { ProfileState.Error }
            }
        }
    }

    private fun searchUsers() {

        _uiState.update { ProfileState.Loading }


    }

    fun navigate(actionState: ActionState) {
        viewModelScope.launch {
            _navigationEvents.send(actionState)
        }
    }

    fun load() {
        loadUserData()
    }

    fun startSearch() {
        _uiState.update { ProfileState.Search }
    }

    fun startUprate() {
        _uiState.update { ProfileState.EditData }
        _state.update { state ->
            state.copy(
                updateFirstName = state.fullName.split(" ")[0],
                updateSecondName = state.fullName.split(" ")[1],
                updatePosition = state.position,
                updateDepartment = state.department,
                updateDescription = state.description
            )
        }
    }

    fun update() {
        updateUser()
    }

    fun onFirstNameChange(firstName: String) {
        _state.update { it.copy(updateFirstName = firstName) }
    }

    fun onSecondNameChange(secondName: String) {
        _state.update { it.copy(updateSecondName = secondName) }
    }

    fun onPositionChange(position: String) {
        _state.update { it.copy(updatePosition = position) }
    }

    fun onDepartmentChange(department: String) {
        _state.update { it.copy(updateDepartment = department) }
    }

    fun onDescriptionChange(description: String) {
        _state.update { it.copy(updateDescription = description) }
    }

    fun onSearchChange(search: String) {
        _state.update { it.copy(search = search) }
    }

//    fun onEditClick() {
//        _state.value = _state.value.copy(isEditing = true)
//    }
//
//    fun onCancelEdit() {
//        _state.value = _state.value.copy(isEditing = false)
//    }
//
//    fun onSaveProfile(updatedData: UserUpdateDto) {
//        viewModelScope.launch {
//            _state.value = _state.value.copy(isLoading = true, error = null)
//
//            val result = updateProfileUseCase(updatedData)
//
//            result.fold(
//                onSuccess = {
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        isEditing = false,
//                        user = _state.value.user?.copy(
//                            firstName = updatedData.firstName,
//                            secondName = updatedData.secondName,
//                            description = updatedData.description,
//                            position = updatedData.position,
//                            department = updatedData.department
//                        )
//                    )
//                },
//                onFailure = { error ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        error = error.message ?: "Ошибка обновления профиля"
//                    )
//                }
//            )
//        }
//    }
//
//    fun updateEditData(newData: UserUpdateDto) {
//        _state.value = _state.value.copy(editData = newData)
//    }
//
//    fun clearError() {
//        _state.value = _state.value.copy(error = null)
//    }
}