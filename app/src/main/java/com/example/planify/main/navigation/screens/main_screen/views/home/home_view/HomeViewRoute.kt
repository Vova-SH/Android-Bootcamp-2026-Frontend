package com.example.planify.main.navigation.screens.main_screen.views.home.home_view

import com.example.planify.core.ui.pager_router_screen.PagerRoute

sealed class HomeViewRoute(override val key: String) : PagerRoute {
    object Day : HomeViewRoute("day")
    object Week : HomeViewRoute("week")
    object Month : HomeViewRoute("month")

    companion object {
        val routes: List<HomeViewRoute> = listOf(Day, Week, Month)
    }
}