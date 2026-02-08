package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.sicampus.bootcamp2026.data.source.SessionManager
import ru.sicampus.bootcamp2026.data.source.TokenStorage
import ru.sicampus.bootcamp2026.ui.screen.AuthRoute
import ru.sicampus.bootcamp2026.ui.screen.CreateMeetingScreen
import ru.sicampus.bootcamp2026.ui.screen.ProfileScreen
import ru.sicampus.bootcamp2026.ui.screen.RegisterScreen
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthViewModel
import ru.sicampus.bootcamp2026.ui.screen.invitations.InvitationsScreen
import ru.sicampus.bootcamp2026.ui.screen.invitations.InvitationsViewModel
import ru.sicampus.bootcamp2026.ui.screen.meetings.MeetingsScreen
import ru.sicampus.bootcamp2026.ui.screen.meetings.MeetingsViewModel
import ru.sicampus.bootcamp2026.ui.screen.profile.ProfileViewModel
import ru.sicampus.bootcamp2026.ui.screen.users.UserDetailsScreen
import ru.sicampus.bootcamp2026.ui.screen.users.UserDetailsViewModel
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenStorage.init(applicationContext)
        if (TokenStorage.accessToken != null) {
            SessionManager.saveSession(TokenStorage.accessToken!!, TokenStorage.userId ?: 0)
        }
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModelFactory = AppViewModelFactory()
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val startDestination = if (TokenStorage.accessToken != null) "meetings" else "auth"
    val showBottomBar = currentRoute in listOf("meetings", "profile", "invitations", "users")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Встречи") },
                        label = { Text("Встречи") },
                        selected = currentRoute == "meetings",
                        onClick = {
                            navController.navigate("meetings") {
                                popUpTo("meetings") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Notifications, contentDescription = "Инвайты") },
                        label = { Text("Инвайты") },
                        selected = currentRoute == "invitations",
                        onClick = {
                            navController.navigate("invitations") {
                                popUpTo("meetings") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Профиль") },
                        label = { Text("Профиль") },
                        selected = currentRoute == "profile",
                        onClick = {
                            navController.navigate("profile") {
                                popUpTo("meetings") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Люди") },
                        label = { Text("Люди") },
                        selected = currentRoute == "users",
                        onClick = {
                            navController.navigate("users") {
                                popUpTo("meetings") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("auth") {
                AuthRoute(
                    viewModel = authViewModel,
                    navigateToHome = {
                        navController.navigate("meetings") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    navigateToRegister = { navController.navigate("register") }
                )
            }

            composable("register") {
                RegisterScreen(
                    onRegisterClick = { dto -> authViewModel.register(dto) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("meetings") {
                val meetingsViewModel: MeetingsViewModel = viewModel(factory = viewModelFactory)
                MeetingsScreen(
                    viewModel = meetingsViewModel,
                    onCreateMeetingClick = { navController.navigate("create_meeting") },
                    onMeetingClick = { meetingId ->
                        navController.navigate("meeting_details/$meetingId")
                    }
                )
            }

            composable(
                route = "meeting_details/{meetingId}",
                arguments = listOf(navArgument("meetingId") { type = NavType.LongType })
            ) { backStackEntry ->
                val meetingId = backStackEntry.arguments?.getLong("meetingId") ?: return@composable
                val detailsViewModel: ru.sicampus.bootcamp2026.ui.screen.meeting_details.MeetingDetailsViewModel = viewModel(factory = viewModelFactory)

                ru.sicampus.bootcamp2026.ui.screen.meeting_details.MeetingDetailsScreen(
                    meetingId = meetingId,
                    viewModel = detailsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("create_meeting") {
                val meetingsViewModel: MeetingsViewModel = viewModel(factory = viewModelFactory)
                CreateMeetingScreen(
                    onCreateClick = { title, desc, place, date, duration ->
                        meetingsViewModel.createMeeting(title, desc, place, date, duration)
                        navController.popBackStack()
                    }
                )
            }

            composable("invitations") {
                val invViewModel: InvitationsViewModel = viewModel(factory = viewModelFactory)
                InvitationsScreen(viewModel = invViewModel)
            }

            composable("profile") {
                val profileViewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
                ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = {
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("users") {
                val usersViewModel: ru.sicampus.bootcamp2026.ui.screen.users.UsersListViewModel = viewModel(factory = viewModelFactory)
                ru.sicampus.bootcamp2026.ui.screen.users.UsersListScreen(
                    viewModel = usersViewModel,
                    onUserClick = { userId ->
                        navController.navigate("user_details/$userId")
                    }
                )
            }

            composable(
                route = "user_details/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.LongType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getLong("userId") ?: return@composable
                val userDetailsViewModel: UserDetailsViewModel = viewModel(factory = viewModelFactory)

                UserDetailsScreen(
                    userId = userId,
                    viewModel = userDetailsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}