package ru.sicampus.bootcamp2026.ui.screens.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.Blue

@Composable
fun BottomNavigation(
    navController: NavController
) {
    val listItems = listOf(
        BottomBarScreen.Schedule,
        BottomBarScreen.CreateInvite,
        BottomBarScreen.Invites,
        BottomBarScreen.Profile
    )
    NavigationBar(
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        listItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route)
                },
                icon = {
                    Icon(painter = painterResource(id = screen.icon), contentDescription = "",
                        tint = Blue)
                },
                label = {
                    Text(text = screen.title,
                        fontSize = 9.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                        color = Blue)
                },

            )
        }
    }
}