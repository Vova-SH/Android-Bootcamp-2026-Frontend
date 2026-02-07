package ru.sicampus.bootcamp2026.ui.screen.register

import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity

sealed interface RegisterState {

    data object Initial : RegisterState
    data class Error( val reason: String ): RegisterState
    data object Loading: RegisterState
    data class Content(
        val user: UserEntity
    ): RegisterState
}