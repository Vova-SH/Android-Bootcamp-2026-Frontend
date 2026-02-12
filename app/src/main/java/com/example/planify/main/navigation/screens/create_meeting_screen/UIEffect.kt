package com.example.planify.main.navigation.screens.create_meeting_screen

import com.example.planify.main.navigation.AppRoute

sealed class UIEffect {
    class Navigate(val route: AppRoute) : UIEffect()
}
