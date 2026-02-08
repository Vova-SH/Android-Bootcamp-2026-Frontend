package com.example.planify.main.navigation.screens.main_screen

import com.example.planify.core.ui.pager_router_screen.PagerRoute
import com.example.planify.main.navigation.AppRoute

sealed class MainScreenRoute(override val key: String) : PagerRoute {
    object Home : MainScreenRoute("home")
    object Chat : MainScreenRoute("chat")
    object Inbox : MainScreenRoute("inbox")
    object Profile : MainScreenRoute("profile")

    companion object {
        val routes: List<MainScreenRoute> = listOf(Home, Chat, Inbox, Profile)
    }
}