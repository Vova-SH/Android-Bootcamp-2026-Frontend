package ru.sicampus.bootcamp2026.ui.theme.components.userList
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase

class UserListViewModel: ViewModel() {

    private val _uiState: MutableStateFlow<UserListState> = MutableStateFlow(UserListState.Content)

    val uiState = _uiState.asStateFlow()

    val getUsersUseCase = GetUsersUseCase(UserRepository(UsersInfoDataSource()))


}