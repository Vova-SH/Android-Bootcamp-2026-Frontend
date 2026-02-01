package ru.sicampus.bootcamp2026.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.presentation.ui.navigation.routes.AuthRoute
import ru.sicampus.bootcamp2026.presentation.ui.navigation.routes.MenuRoute
import ru.sicampus.bootcamp2026.presentation.ui.navigation.routes.RegRoute
import ru.sicampus.bootcamp2026.presentation.ui.navigation.routes.StartRoute
import ru.sicampus.bootcamp2026.presentation.ui.screens.main.MenuScreen
import ru.sicampus.bootcamp2026.presentation.ui.screens.start.StartScreen
import ru.sicampus.bootcamp2026.presentation.ui.screens.start.auth.AuthScreen
import ru.sicampus.bootcamp2026.presentation.ui.screens.start.reg.RegScreen


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = StartRoute,
    ) {
        composable<StartRoute> { StartScreen(nav = navController) }
        composable<AuthRoute> { AuthScreen(nav = navController) }
        composable<RegRoute> { RegScreen(nav = navController) }
        composable<MenuRoute> { MenuScreen(nav = navController) }
    }
}