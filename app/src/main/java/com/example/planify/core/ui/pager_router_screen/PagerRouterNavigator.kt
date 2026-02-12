package com.example.planify.core.ui.pager_router_screen

interface PagerRouterNavigator {
    val currentRouteIndex: Int
    val currentRoute: PagerRoute

    fun navigateTo(route: PagerRoute)

    fun navigateTo(route: String)

    fun navigateTo(index: Int)

}