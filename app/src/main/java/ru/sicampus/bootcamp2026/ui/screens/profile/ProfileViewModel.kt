//package ru.sicampus.bootcamp2026.ui.screens.profile
//
//import android.os.Build
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import ru.sicampus.bootcamp2026.data.dto.user.UserDto
//import ru.sicampus.bootcamp2026.data.dto.user.UserUpdateDto
//import ru.sicampus.bootcamp2026.domain.usecase.UpdateProfileUseCase
//import java.time.LocalDateTime
//
//
//class ProfileViewModel(
//    private val updateProfileUseCase: UpdateProfileUseCase
//) : ViewModel() {
//
////    private val _state = MutableStateFlow(ProfileState())
////    val state: StateFlow<ProfileState> = _state.asStateFlow()
////
////    init {
////        loadUserData()
////    }
////
////    private fun loadUserData() {
////        //пока так
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            _state.value = _state.value.copy(
////                user = UserDto(
////                    id = 1,
////                    firstName = "Алексей",
////                    secondName = "Петров",
////                    description = "Я — великий Frontend-разработчик. Моя главная задача — превращать идеи дизайнеров и требования бизнеса в быстрые, удобные и красивые интерфейсы спомощью котлина.",
////                    position = "Frontend-Разработчик",
////                    department = "Разработка продуктов",
////                    photoUrl = "",
////                    role = "ROLE_USER",
////                    createdAt = LocalDateTime.now(),
////                    updatedAt = LocalDateTime.now()
////                ),
////                editData = UserUpdateDto(
////                    id = 1,
////                    firstName = "Алексей",
////                    secondName = "Петров",
////                    description = "Я — Frontend-разработчик. Моя главная задача — превращать идеи дизайнеров и требования бизнеса в быстрые, удобные и красивые интерфейсы.",
////                    position = "Frontend-Разработчик",
////                    department = "Разработка продуктов"
////                )
////            )
////        }
////    }
////
////    fun onEditClick() {
////        _state.value = _state.value.copy(isEditing = true)
////    }
////
////    fun onCancelEdit() {
////        _state.value = _state.value.copy(isEditing = false)
////    }
////
////    fun onSaveProfile(updatedData: UserUpdateDto) {
////        viewModelScope.launch {
////            _state.value = _state.value.copy(isLoading = true, error = null)
////
////            val result = updateProfileUseCase(updatedData)
////
////            result.fold(
////                onSuccess = {
////                    _state.value = _state.value.copy(
////                        isLoading = false,
////                        isEditing = false,
////                        user = _state.value.user?.copy(
////                            firstName = updatedData.firstName,
////                            secondName = updatedData.secondName,
////                            description = updatedData.description,
////                            position = updatedData.position,
////                            department = updatedData.department
////                        )
////                    )
////                },
////                onFailure = { error ->
////                    _state.value = _state.value.copy(
////                        isLoading = false,
////                        error = error.message ?: "Ошибка обновления профиля"
////                    )
////                }
////            )
////        }
////    }
////
////    fun updateEditData(newData: UserUpdateDto) {
////        _state.value = _state.value.copy(editData = newData)
////    }
////
////    fun clearError() {
////        _state.value = _state.value.copy(error = null)
////    }
//}