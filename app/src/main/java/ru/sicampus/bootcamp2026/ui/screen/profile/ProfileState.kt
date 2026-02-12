package ru.sicampus.bootcamp2026.ui.screen.profile

import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity

sealed interface ProfileState {
    data class Error( val reason: String ): ProfileState
    data object Loading: ProfileState
    data object Content: ProfileState
}