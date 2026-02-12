package ru.sicampus.bootcamp2026.ui.root

import android.content.Context
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.root.nav.ItemsNav
import ru.sicampus.bootcamp2026.ui.screens.book.BookScreen
import ru.sicampus.bootcamp2026.ui.screens.incomingbooks.IncomingScreen
import ru.sicampus.bootcamp2026.ui.screens.profile.ProfileScreen
import ru.sicampus.bootcamp2026.ui.screens.schedule.DetailsMeetingScreen
import ru.sicampus.bootcamp2026.ui.screens.schedule.ScheduleScreen
import ru.sicampus.bootcamp2026.ui.screens.schedule.ScheduleViewModel
import ru.sicampus.bootcamp2026.ui.screens.schedule.ScheduleViewModelFactory

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    padding: PaddingValues,
    context: Context
) {
    val sviewModel: ScheduleViewModel = viewModel(factory = ScheduleViewModelFactory.create(context))
    val index = remember { mutableStateOf(0) }
    NavHost(
        navController = navController,
        startDestination = ItemsNav.BottomNavItems[0].route,
        modifier = Modifier.padding(paddingValues = padding),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        composable(ItemsNav.BottomNavItems[0].route) {
            ScheduleScreen(context = context, navController = navController,
                sviewModel, index)
        }

        composable(ItemsNav.BottomNavItems[1].route) {
            IncomingScreen(context = context)
        }

        composable(ItemsNav.BottomNavItems[2].route) {
            ProfileScreen(context = context)
        }

        composable(ItemsNav.BottomNavItems[3].route) {
            BookScreen(navController, context)
        }
        composable(ItemsNav.BottomNavItems[4].route) {
            DetailsMeetingScreen(  context =  context, vm = sviewModel, index = index.value)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(containerColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        ItemsNav.BottomNavItems.subList(fromIndex = 0 ,toIndex = 3).forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(painterResource(navItem.icon), contentDescription = navItem.label)
                },
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = true,

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xff155DFC),
                    unselectedIconColor = Color(0xffB3C1DE),
                    selectedTextColor = Color(0xff155DFC),
                    unselectedTextColor = Color(0xffB3C1DE),
                    indicatorColor = Color.White
                )
            )
        }
    }

}
