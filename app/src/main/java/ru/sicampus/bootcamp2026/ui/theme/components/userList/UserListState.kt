package ru.sicampus.bootcamp2026.ui.theme.components.userList

sealed interface UserListState{
    data class Error(val reason: String):UserListState
    data object Loading:UserListState
    data object Content: UserListState
}