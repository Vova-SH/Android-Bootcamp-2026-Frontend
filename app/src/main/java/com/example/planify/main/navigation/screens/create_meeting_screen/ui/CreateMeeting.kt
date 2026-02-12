package com.example.planify.main.navigation.screens.create_meeting_screen.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planify.core.ui.pager_router_screen.PagerRouterScreen
import com.example.planify.core.ui.pager_router_screen.rememberPagerRouterScreenState
import com.example.planify.main.navigation.screens.create_meeting_screen.CreateMeetingRoute
import com.example.planify.main.navigation.screens.create_meeting_screen.CreateMeetingViewModel
import com.example.planify.main.navigation.screens.create_meeting_screen.UIEffect
import com.example.planify.main.navigation.screens.create_meeting_screen.components.BottomBar
import com.example.planify.main.navigation.screens.create_meeting_screen.components.TopBar
import com.example.planify.main.navigation.screens.create_meeting_screen.ui.steps.CreateMeetingStep1
import com.example.planify.main.navigation.screens.create_meeting_screen.ui.steps.CreateMeetingStep2
import com.example.planify.main.navigation.screens.create_meeting_screen.ui.steps.CreateMeetingStep3

@Composable
fun CreateMeeting(
    onBack: () -> Unit,
    navController: NavController
) {
    CreateMeeting(
        viewModel = hiltViewModel(),
        navController = navController,
        onBack = onBack
    )
}

@Composable
private fun CreateMeeting(
    viewModel: CreateMeetingViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.effects.collect { event ->
            when (event) {
                is UIEffect.Navigate -> navController.navigate(event.route.route)
            }
        }
    }

    val pagerRouterState = rememberPagerRouterScreenState(
        routes = CreateMeetingRoute.routes,
        startRoute = CreateMeetingRoute.Info
    )

    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = colors.background,
        bottomBar = {
            BottomBar(
                navigator = pagerRouterState,
                viewModel = viewModel
            )
        },
        topBar = {
            TopBar(
                currentPage = pagerRouterState.currentRouteIndex,
                onBack = onBack
            )
        }
    ) { padding ->
        PagerRouterScreen(
            modifier = Modifier
                .padding(padding)
                .wrapContentHeight()
                .animateContentSize(),
            userScrollEnabled = false,
            state = pagerRouterState
        ) {
            screen(CreateMeetingRoute.Info) {
                CreateMeetingStep1(viewModel)
            }
            screen(CreateMeetingRoute.Time) {
                CreateMeetingStep2(viewModel)
            }
            screen(CreateMeetingRoute.Participants) {
                CreateMeetingStep3(viewModel)
            }
        }
    }
}