package ru.sicampus.bootcamp2026.android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.android.ui.nav.*
import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.android.ui.theme.IconsGrey
import ru.sicampus.bootcamp2026.android.ui.theme.White

@Composable
fun CustomNavigationBar(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        containerColor = White,
        tonalElevation = 8.dp,
    ) {
        // Home
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Home",
                    modifier = Modifier.size(45.dp),
                    tint = if (currentRoute == HomeRoute::class.qualifiedName)
                        DarkBlue else IconsGrey
                )
            },
            selected = currentRoute == HomeRoute::class.qualifiedName,
            onClick = {
                navController.navigateBottom(HomeRoute)
            }
        )

        // Create
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.AddCircleOutline,
                    contentDescription = "Create",
                    modifier = Modifier.size(45.dp),
                    tint = if (currentRoute == CreateMeetingRoute::class.qualifiedName)
                        DarkBlue else IconsGrey
                )
            },
            selected = currentRoute == CreateMeetingRoute::class.qualifiedName,
            onClick = {
                navController.navigateBottom(CreateMeetingRoute)
            }
        )

        // Profile
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier.size(45.dp),
                    tint = if (currentRoute == ProfileRoute::class.qualifiedName)
                        DarkBlue else IconsGrey
                )
            },
            selected = currentRoute == ProfileRoute::class.qualifiedName,
            onClick = {
                navController.navigateBottom(ProfileRoute)
            }
        )
    }
}

@Preview
@Composable
fun SHow(){
    CustomNavigationBar()
}
