package ru.innovationcampus.android.ui.nav

import android.util.Log
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import ru.innovationcampus.android.data.source.AuthLocalDataSource
import ru.innovationcampus.android.ui.screen.auth.AuthScreen
import ru.innovationcampus.android.ui.screen.list.ListScreen
import ru.innovationcampus.android.ui.screen.meetings.MeetingsListScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val currentToken = runBlocking { AuthLocalDataSource.getToken() }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (currentToken == null) AuthRoute else ListRoute
    ) {
        composable<AuthRoute> {
            AuthScreen(
                navController = navController
            )
        }
        composable<ListRoute> {
            ListScreen(
                navController = navController
            )
        }
        composable<MeetingsListRoute> {
            MeetingsListScreen(
                navController = navController
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState();
        val currentRoute = backStackEntry?.destination?.route

        Log.d("NavRoute","${currentRoute.toString()} ${AuthRoute::class.qualifiedName.toString()}")

        NavigationBarItem(
            selected = currentRoute == MeetingsListRoute::class.qualifiedName,
            onClick = { navController.navigate(MeetingsListRoute) },
            icon = { Text("Встречи") }
        )

        NavigationBarItem(
            selected = currentRoute == ListRoute::class.qualifiedName,
            onClick = { navController.navigate(ListRoute) },
            icon = { Text("Пользователи") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                AuthLocalDataSource.clearToken()
                navController.navigate(AuthRoute) {
                    popUpTo(navController.graph.id);
                }
            },
            icon = { Text("Выйти") }
        )

    }
}