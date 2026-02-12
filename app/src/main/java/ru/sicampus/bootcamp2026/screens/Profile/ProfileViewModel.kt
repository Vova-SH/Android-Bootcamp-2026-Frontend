package ru.sicampus.bootcamp2026.screens.Profile
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import ru.sicampus.bootcamp2026.data.UserRepository
//import ru.sicampus.bootcamp2026.domain.entities.UserEntity
//
//class ProfileViewModel(
//    private val userRepository: UserRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
//    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
//
//    init {
//        loadUserData()
//    }
//
//    fun loadUserData() {
//        viewModelScope.launch {
//            _uiState.value = ProfileUiState.Loading
//            try {
//                val result = userRepository.getCurrentUser()
//                result.onSuccess { userEntity ->
//                    _uiState.value = ProfileUiState.Success(userEntity)
//                }.onFailure { exception ->
//                    _uiState.value = ProfileUiState.Error(
//                        message = exception.message ?: "Ошибка загрузки данных"
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.value = ProfileUiState.Error(
//                    message = "Сетевая ошибка: ${e.message ?: "неизвестная ошибка"}"
//                )
//            }
//        }
//    }
//
//    fun refresh() {
//        loadUserData()
//    }
//}
//
//sealed class ProfileUiState {
//    object Loading : ProfileUiState()
//    data class Error(val message: String) : ProfileUiState()
//    data class Success(val user: UserEntity) : ProfileUiState()
//}