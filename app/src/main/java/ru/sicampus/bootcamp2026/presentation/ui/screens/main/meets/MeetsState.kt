package ru.sicampus.bootcamp2026.presentation.ui.screens.main.meets

import ru.sicampus.bootcamp2026.domain.models.meet.Meet

sealed interface MeetsState {
    data object Loading: MeetsState
    data class Error(
        val reason: String
    ): MeetsState
    data class Content(
        val users: List<Meet>
    ) : MeetsState
}