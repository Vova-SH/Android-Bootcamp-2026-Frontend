package ru.sicampus.bootcamp2026.ui.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.navigation.AppNavGraph

@Composable
fun MainApp() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}
