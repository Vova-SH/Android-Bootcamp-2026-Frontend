package ru.sicampus.bootcamp2026.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.network.UserRepository
import ru.sicampus.bootcamp2026.data.network.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetAuthUserUseCase
import ru.sicampus.bootcamp2026.domain.GetUserUseCase
import ru.sicampus.bootcamp2026.selectedUser

class ProfileViewModel : ViewModel() {
    private val getAuthUserUseCase = GetAuthUserUseCase(
        userRepository = UserRepository(UserInfoDataSource())
    )

    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Loading)

    val uiState = _uiState.asStateFlow()
    var isCurrentUser : Boolean = false
    var isInited = false

    fun init(isCurrentUser : Boolean) {
        if (isInited) return
        this.isCurrentUser = isCurrentUser
        getData()
        isInited = true
    }


    fun getData() {
        viewModelScope.launch {
            _uiState.emit(ProfileState.Loading)
            if (isCurrentUser) {
                getAuthUserUseCase.invoke().fold(
                    onSuccess = { data ->
                        _uiState.emit(ProfileState.Content(data))
                    },
                    onFailure = { error ->
                        Log.e("ui state", error.message.orEmpty())
                        _uiState.emit(ProfileState.Error(error.message.orEmpty()) { getData() })
                    }
                )
            } else {
                selectedUser?.let {
                    _uiState.emit(ProfileState.Content(it))
                } ?:
                _uiState.emit(ProfileState.Error("Не удалось загрузуть контакт") { getData() })
            }
        }
    }

}