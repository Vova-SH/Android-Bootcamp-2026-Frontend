package com.example.planify.core.ui.pager_router_screen

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
private fun PagerHost(
    state: PagerRouterState,
    modifier: Modifier = Modifier,
    userScrollEnabled: Boolean = true,
    content: @Composable (page: PagerRoute) -> Unit
) {
    HorizontalPager(
        modifier = modifier,
        userScrollEnabled = userScrollEnabled,
        state = state.pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->
        content(state.routes[page])
    }
}

@Composable
fun rememberPagerRouterScreenState(
    routes: List<PagerRoute>,
    startRoute: PagerRoute? = null
) : PagerRouterState {
    val coroutineScope = rememberCoroutineScope()

    val initialIndex = startRoute?.let { routes.indexOfFirst { r -> r.key == it.key } } ?: 0

    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { routes.size }
    )

    return remember {
        PagerRouterState(
            coroutineScope = coroutineScope,
            routes = routes,
            pagerState = pagerState
        )
    }
}

@Composable
fun PagerRouterScreen(
    modifier: Modifier = Modifier,
    state: PagerRouterState,
    userScrollEnabled: Boolean = true,
    build: PagerRouterBuilder.() -> Unit
) {
    val graph = remember {
        PagerRouterBuilder().apply(build)
    }

    PagerHost(
        modifier = modifier,
        userScrollEnabled = userScrollEnabled,
        state = state
    ) { route ->
        graph.destinations[route.key]?.invoke() ?: error("No screen registered for route ${route.key}")
    }
}


