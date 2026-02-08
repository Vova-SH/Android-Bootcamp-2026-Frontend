package com.example.planify.main.navigation.screens.main_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planify.core.ui.pager_router_screen.PagerRouterScreen
import com.example.planify.core.ui.pager_router_screen.rememberPagerRouterScreenState
import com.example.planify.main.navigation.screens.chat_screen.ChatScreen
import com.example.planify.main.navigation.screens.create_meeting_screen.components.create_meeting_floating_dialog.CreateMeetingDialog
import com.example.planify.main.navigation.screens.inbox_screen.ui.InboxView
import com.example.planify.main.navigation.screens.main_screen.components.BottomNavBar
import com.example.planify.main.navigation.screens.main_screen.components.TopBar
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.ui.HomeView
import com.example.planify.main.navigation.screens.main_screen.views.profile.ui.ProfileView

@Composable
fun MainScreenBox(
    onSettings: () -> Unit,
    onCreateClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onNotifications: () -> Unit,
    navController: NavController
) {
    val colors = MaterialTheme.colorScheme

    var opened by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainScreen(
            onSettings = onSettings,
            onOpen = { opened = true },
            viewModel = hiltViewModel(),
            onEditProfileClick = onEditProfileClick,
            onNotifications = onNotifications,
            navController = navController
        )

        AnimatedVisibility(
            visible = opened,
            exit = fadeOut(),
            enter = fadeIn(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { opened = false }
            )
        }
        AnimatedVisibility(
            visible = opened,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.42f)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                tonalElevation = 6.dp,
                shadowElevation = 12.dp,
                shape = MaterialTheme.shapes.extraLarge,
                color = colors.background
            ) {
                CreateMeetingDialog(
                    onClose = { opened = false },
                    onCreateClick = onCreateClick
                )
            }
        }
    }
}

@Composable
private fun MainScreen(
    onSettings: () -> Unit,
    onOpen: () -> Unit,
    onEditProfileClick: () -> Unit,
    onNotifications: () -> Unit,
    navController: NavController,
    viewModel: MainScreenViewModel
) {
    val router = rememberPagerRouterScreenState(
        routes = MainScreenRoute.routes,
        startRoute = MainScreenRoute.Home // <-------
    )
    val colors = MaterialTheme.colorScheme

    var monthTitle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                pagerRouter = router,
                monthTitle = monthTitle,
                onSettings = onSettings,
                onNotifications = onNotifications
            )
        },
        bottomBar = {
            BottomNavBar(
                pagerRouter = router,
                onOpenCreateDialog = onOpen
            )
        },
        containerColor = colors.background
    ) { padding ->
        PagerRouterScreen(
            modifier = Modifier.fillMaxSize(),
            state = router
        ) {
            screen(MainScreenRoute.Home) {
                HomeView(
                    navController = navController,
                    scaffoldPadding = padding,
                    setMonthTitle = { monthTitle = it }
                )
            }
            screen(MainScreenRoute.Chat) {
                ChatScreen(
                    scaffoldPadding = padding
                )
            }
            screen(MainScreenRoute.Inbox) { InboxView(
                scaffoldPadding = padding
            ) }
            screen(MainScreenRoute.Profile) {
                ProfileView(
                    scaffoldPadding = padding,
                    onEditClick = onEditProfileClick,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun Screen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("123123123")
    }
}