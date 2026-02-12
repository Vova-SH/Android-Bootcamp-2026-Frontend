package com.example.planify.main.navigation.screens.main_screen.views.profile

import com.example.planify.core.ui.pager_router_screen.PagerRoute

sealed class ProfileViewRoute(override val key: String) : PagerRoute {
    object Profile : ProfileViewRoute("profile")
    object Settings : ProfileViewRoute("settings")

    companion object {
        val routes: List<ProfileViewRoute> = listOf(Profile, Settings)
    }
}