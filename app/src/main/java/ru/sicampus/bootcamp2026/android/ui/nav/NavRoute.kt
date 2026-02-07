package ru.sicampus.bootcamp2026.android.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.ui.testScreens.auth.AuthScreen
import ru.sicampus.bootcamp2026.android.ui.testScreens.home.HomeScreen
import ru.sicampus.bootcamp2026.android.ui.testScreens.home.CreateMeetingScreen
import ru.sicampus.bootcamp2026.android.ui.testScreens.home.HomeViewModel
import ru.sicampus.bootcamp2026.android.ui.testScreens.home.NotificationScreen
import ru.sicampus.bootcamp2026.android.ui.testScreens.profile.ProfileScreen
import ru.sicampus.bootcamp2026.android.ui.testScreens.signUp.SignUpScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val currentToken = runBlocking { AuthLocalDataSource.getToken() }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (currentToken == null) AuthRoute else HomeRoute
    ) {
        // Экран авторизации
        composable<AuthRoute> {
            AuthScreen(
                navController = navController
            )
        }

        // Экран регистрации
        composable<SignUpRoute> {
            SignUpScreen(
                navController = navController
            )
        }

        // Главный экран
        composable<HomeRoute> { backStackEntry ->
            val homeViewModel: HomeViewModel = viewModel(backStackEntry)
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel,
                onNotificationsClick = {
                    navController.navigate(NotificationsRoute)
                }
            )
        }

        // Экран создания встречи
        composable<CreateMeetingRoute> {
            CreateMeetingScreen(
                navController = navController,
            )
        }

        // Экран профиля
        composable<ProfileRoute> {
            ProfileScreen(
                navController = navController,
            )
        }

        composable<NotificationsRoute> {
            NotificationScreen(
                onExitClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun NavHostController.navigateBottom(route: Any) {
    navigate(route) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}