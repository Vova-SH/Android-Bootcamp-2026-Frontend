package com.teto.planner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.teto.planner.presentation.navigation.AppNavHost
import com.teto.planner.presentation.navigation.NavigationItem
import com.teto.planner.presentation.navigation.bottomNavItems
import com.teto.planner.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme{
                MainApp()
            }
        }
    }
}


@Composable
fun MainApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isBottomBarVisible = bottomNavItems.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
    }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                MyBottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

@Composable
fun MyBottomNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.screen.route
            } == true

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(NavigationItem.Schedule.screen.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}