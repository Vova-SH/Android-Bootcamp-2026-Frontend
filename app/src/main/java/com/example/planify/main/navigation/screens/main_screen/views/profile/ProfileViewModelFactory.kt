package com.example.planify.main.navigation.screens.main_screen.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.planify.main.features.auth.domain.services.UsersService
import com.example.planify.main.features.profile.domain.services.ProfilesService

class ProfileViewModelFactory(
    val profileService: ProfilesService,
    val usersService: UsersService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(
                profilesService = profileService,
                usersService = usersService
                ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}