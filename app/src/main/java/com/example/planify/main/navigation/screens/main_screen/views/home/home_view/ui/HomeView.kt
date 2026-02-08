package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planify.core.ui.pager_router_screen.PagerRouterScreen
import com.example.planify.core.ui.pager_router_screen.rememberPagerRouterScreenState
import com.example.planify.main.common.utils.pageForDate
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.HomeViewModel
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.HomeViewRoute
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.TopNavBar
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.ui.ui_components.day_view.HomeDayView
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.ui.ui_components.month_view.HomeMonthView
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.ui.ui_components.week_view.HomeWeekView

@Composable
private fun HomeView(
    viewModel: HomeViewModel,
    scaffoldPadding: PaddingValues,
    navController: NavController,
    setMonthTitle: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.navigateEvents.collect { route ->
            navController.navigate(route.route)
        }
    }

    val router = rememberPagerRouterScreenState(
        routes = HomeViewRoute.routes,
        startRoute = HomeViewRoute.Week
    )

    val initialScrollPage = 5000
    val scrollPagerState = rememberPagerState(
        initialPage = initialScrollPage,
        pageCount = { 10000 }
    )

    val initialCalendarPage = 500
    val calendarPagerState = rememberPagerState(
        initialPage = initialCalendarPage,
        pageCount = { 1000 }
    )

    val selectedDate by viewModel.selectedDate.collectAsState()

    // Sync calendarPager and ScrollPager(Day + BottomWeek scroll)
    LaunchedEffect(selectedDate) {
        val target = selectedDate.pageForDate(initialPage = initialScrollPage)

        if (!scrollPagerState.isScrollInProgress && scrollPagerState.currentPage != target) {
            scrollPagerState.scrollToPage(target)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = scaffoldPadding.calculateTopPadding()
            ),
        topBar = { TopNavBar(pagerRouter = router) }
    ) { padding ->
        PagerRouterScreen(
            modifier = Modifier
                .padding(padding),
            userScrollEnabled = false,
            state = router
        ) {
            screen(HomeViewRoute.Day) {
                HomeDayView(
                    viewModel = viewModel,
                    selectedDate = selectedDate,
                    onDateSelected = { viewModel.onDateSelected(it) },
                    scrollPagerState = scrollPagerState,
                    initialPage = initialScrollPage,
                    getMeetingsInfoByDate = { viewModel.getMeetingsInfoByDate(it) }
                )
            }

            screen(HomeViewRoute.Week) {
                HomeWeekView(
                    viewModel = viewModel,
                    selectedDate = selectedDate,
                    onMeetingClick = { viewModel.meetingClick(it) },
                    scrollPagerState = scrollPagerState,
                    initialPageBottom = initialScrollPage,
                    onDateSelected = { viewModel.onDateSelected(it) },
                    getMeetingsInfoByDate = { viewModel.getMeetingsInfoByDate(it) },
                    setMonthTitle = { setMonthTitle(it) }
                )
            }

            screen(HomeViewRoute.Month) {
                HomeMonthView(
                    viewModel = viewModel,
                    selectedDate = selectedDate,
                    pagerState = calendarPagerState,
                    initialPage = initialCalendarPage,
                    onDateSelected = { viewModel.onDateSelected(it) },
                    getMeetingsInfoByDate = { viewModel.getMeetingsInfoByDate(it) },
                    scaffoldPadding = scaffoldPadding
                )
            }
        }
    }

}

@Composable
fun HomeView(
    navController: NavController,
    scaffoldPadding: PaddingValues,
    setMonthTitle: (String) -> Unit
) {
    HomeView(
        viewModel = hiltViewModel(),
        scaffoldPadding = scaffoldPadding,
        setMonthTitle = setMonthTitle,
        navController = navController
    )
}