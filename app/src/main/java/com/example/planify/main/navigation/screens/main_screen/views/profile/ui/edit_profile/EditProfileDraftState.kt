package com.example.planify.main.navigation.screens.main_screen.views.profile.ui.edit_profile

import com.example.planify.main.features.profiles.domain.entities.Profile

data class EditProfileDraftState (
    val firstName: String?,
    val lastName: String?,
    val department: String?,
    val position: String?,
    val profileImageUrl: String?
) {
    companion object {
        fun empty(): EditProfileDraftState = EditProfileDraftState(
            firstName = null,
            lastName = null,
            department = null,
            position = null,
            profileImageUrl = null
        )

        fun fromProfile(originalProfile: Profile) = EditProfileDraftState(
            firstName = originalProfile.firstName,
            lastName = originalProfile.lastName,
            department = originalProfile.department,
            position = originalProfile.position,
            profileImageUrl = originalProfile.profileImageUrl
        )
    }
}