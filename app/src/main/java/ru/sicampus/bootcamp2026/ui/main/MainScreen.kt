package ru.sicampus.bootcamp2026.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.sicampus.bootcamp2026.ui.calendar.CalendarScreen
import ru.sicampus.bootcamp2026.ui.components.HomeBottomBar
import ru.sicampus.bootcamp2026.ui.home.HomeScreen
import ru.sicampus.bootcamp2026.ui.notification.NotificationScreen

@Composable
fun MainScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var currentTab by remember { mutableIntStateOf(1) }

    //Scaffold без фона (фон в AppNavGraph)
    Scaffold(
            containerColor = Color.Transparent, // Прозрачный, чтобы видеть картинку
             contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                HomeBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            }
        ) { innerPadding ->

            //Контент экранов
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    0 -> CalendarScreen()
                    1 -> HomeScreen(
                        onNavigateToCreate = onNavigateToCreate,
                        onNavigateToDetails = onNavigateToDetails,
                        onNavigateToProfile = onNavigateToProfile
                    )
                    2 -> NotificationScreen(
                        onNavigateToDetails = onNavigateToDetails
                    )
                }
            }
        }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        onNavigateToCreate = {},
        onNavigateToDetails = {},
        onNavigateToProfile = {}
    )
}