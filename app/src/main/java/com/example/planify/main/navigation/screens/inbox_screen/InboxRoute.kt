package com.example.planify.main.navigation.screens.inbox_screen

import com.example.planify.core.ui.pager_router_screen.PagerRoute
import com.example.planify.main.navigation.screens.main_screen.MainScreenRoute

sealed class InboxRoute(override val key: String): PagerRoute {

    object Incoming: InboxRoute("incoming")

    object Sent: InboxRoute("sent")

    companion object {
        val routes: List<InboxRoute> = listOf(Incoming, Sent)
    }

}