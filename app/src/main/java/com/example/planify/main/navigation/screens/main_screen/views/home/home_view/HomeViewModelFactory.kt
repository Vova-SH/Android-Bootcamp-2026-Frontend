package com.example.planify.main.navigation.screens.main_screen.views.home.home_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.planify.main.features.meeting.domain.services.MeetingService

class HomeViewModelFactory(
    val meetingService: MeetingService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(meetingService = meetingService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}