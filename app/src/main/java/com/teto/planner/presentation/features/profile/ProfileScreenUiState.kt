package com.teto.planner.presentation.features.profile

import com.teto.planner.domain.model.user.UserMe

sealed interface ProfileScreenUiState {
    data object Loading : ProfileScreenUiState

    data class Success(
        val user: UserMe,
        val name: String = "",
        val telegram: String = "",
        val bio: String = "",
        val isEditing: Boolean = false,
        val isSubmitting: Boolean = false,
        val isSaveSuccessful: Boolean = false
    ) : ProfileScreenUiState {
        val hasChanges: Boolean
            get() = name != user.name ||
                    telegram != (user.telegram ?: "") ||
                    bio != (user.bio ?: "")

        val canSave: Boolean get() = isEditing && hasChanges && !isSubmitting
    }

    data class Error(val message: String) : ProfileScreenUiState
}