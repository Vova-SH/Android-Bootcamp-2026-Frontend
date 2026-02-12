package com.example.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.authorization.presentation.AuthMainScreen
import com.example.authorization.presentation.AuthScreenViewModel
import com.example.create_meet.presentation.AddMeetingScreen
import com.example.create_meet.presentation.AddMeetingViewModel
import com.example.create_meet.presentation.EventsListScreen
import com.example.create_meet.presentation.EventsListScreenViewModel
import com.example.registration.presentation.RegisterMainScreen
import com.example.registration.presentation.RegisterScreenViewModel
import com.example.user_main.presentation.ProfileScreen
import com.example.user_main.presentation.UserMainScreenViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthScreenViewModel,
    registerScreenViewModel: RegisterScreenViewModel,
    navigationViewModel: NavigationViewModel,
    userMainScreenViewModel: UserMainScreenViewModel,
    eventsListScreenViewModel: EventsListScreenViewModel,
) {
    val navController = rememberNavController()
    val token by navigationViewModel.hasToken.collectAsState()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val startDestination =
        if (!token) NavigationScreens.AUTHORIZATION.routeName
        else NavigationScreens.MAIN.routeName

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationScreens.AUTHORIZATION.routeName) {
            AuthMainScreen(authViewModel, onRegisterClick = {
                navController.navigate(NavigationScreens.REGISTER.routeName)
            }, onLoginSuccess = {
                navController.navigate(BottomNavScreen.Profile.route) {
                    popUpTo(NavigationScreens.AUTHORIZATION.routeName) { inclusive = true }
                }
            })
        }

        composable(NavigationScreens.REGISTER.routeName) {
            RegisterMainScreen(registerScreenViewModel, onBack = {
                navController.navigate(
                    NavigationScreens.AUTHORIZATION.routeName
                )
            }, onRegisterSuccess = {
                navController.navigate(BottomNavScreen.Profile.route) {
                    popUpTo(NavigationScreens.AUTHORIZATION.routeName) { inclusive = true }
                }
            }
            )
        }
        composable(BottomNavScreen.Profile.route) {
            MainWithNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(BottomNavScreen.Profile.route)
                    }
                }
            ) {
                ProfileScreen(
                    viewModel = userMainScreenViewModel,
                    onLogoutClick = {
                        navController.navigate(NavigationScreens.AUTHORIZATION.routeName)

                    }
                )
            }
        }
        composable(BottomNavScreen.Other.route) {
            MainWithNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(BottomNavScreen.Profile.route)
                    }
                },
                profileContent = {
                    EventsListScreen(
                        viewModel = eventsListScreenViewModel,
                        onAddNewMeet = {
                            navController.navigate(NavigationScreens.ADD_NEW_MEETING.routeName)
                        }
                    )
                }
            )
        }

        composable(NavigationScreens.ADD_NEW_MEETING.routeName) {
            val addMeetingViewModel: AddMeetingViewModel = hiltViewModel()

            AddMeetingScreen(
                addMeetingViewModel = addMeetingViewModel
            ) {
                navController.navigate(BottomNavScreen.Other.route) {
                    popUpTo(NavigationScreens.ADD_NEW_MEETING.routeName) {
                        inclusive = true
                    }
                }

            }
        }

    }
}

@Composable
fun MainWithNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    profileContent: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavScreen.entries.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (item) {
                                    BottomNavScreen.Profile -> Icons.Default.Person
                                    BottomNavScreen.Other -> Icons.Default.Star
                                },
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            profileContent()
        }
    }
}
