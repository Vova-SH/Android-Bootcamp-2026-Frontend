package com.example.planify.core.ui.pager_router_screen

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class PagerRouterState internal constructor(
    internal val coroutineScope: CoroutineScope,
    internal val pagerState: PagerState,
    internal val routes: List<PagerRoute>
) : PagerRouterNavigator {
    private var navigating = false

    override val currentRouteIndex: Int
        get() = pagerState.currentPage

    override val currentRoute: PagerRoute
        get() = routes[pagerState.currentPage]

    override fun navigateTo(route: PagerRoute) {
        val index = routes.indexOf(route)
        if (index >= 0) navigateTo(index)
    }

    override fun navigateTo(route: String) {
        val index = routes.indexOfFirst{ it.key == route }
        if (index >= 0) navigateTo(index)
    }

    override fun navigateTo(index: Int) {
        if (navigating) return
        navigating = true

        coroutineScope.launch {
            pagerState.animateScrollToPage(index)
            navigating = false
        }
    }
}
