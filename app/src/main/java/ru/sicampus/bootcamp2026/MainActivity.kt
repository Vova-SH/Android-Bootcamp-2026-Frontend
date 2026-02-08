package ru.sicampus.bootcamp2026

//import ru.sicampus.bootcamp2026.ui.screen.meetings.MeetingsScreen
//import ru.sicampus.bootcamp2026.ui.screen.meeting.Meeting
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthScreen
import ru.sicampus.bootcamp2026.ui.screen.meeting.Meeting
import ru.sicampus.bootcamp2026.ui.screen.meetings.MeetingsScreen
import ru.sicampus.bootcamp2026.ui.screen.profile.ProfileScreen
import ru.sicampus.bootcamp2026.ui.screen.users.UsersScreen
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0eWdoQGJrLnJ1IiwiaWF0IjoxNzcwNTY2MDY5LCJleHAiOjE3NzA1Njk2Njl9.1-peDdxCOW-PyMYpSUk6hWu0POuwYmMOQK1MwCuxBK4"

var currentUser: UserEntity? = null // TODO в дальнейшем заменить на авторизацию
var selectedUser: UserEntity? = null
var selectedMeeting : MeetingEntity? = null
var historyOfUserSearch = mutableListOf<String>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme {
                val navController = rememberNavController()


                NavBar(
                    Modifier
                        .fillMaxSize(),
                    navController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    // Определяем элементы для нижней панели
    val navItems = listOf(
        NavItem("Приглашения", NavRoutes.Meetings.route, Icons.Filled.CalendarToday, Icons.Outlined.CalendarToday),
        NavItem("Пользователи", NavRoutes.Users.route, Icons.Filled.People, Icons.Outlined.People),
        NavItem("Профиль", NavRoutes.Profile.route, Icons.Filled.Person, Icons.Outlined.Person),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in navItems.map { it.route }

    Scaffold(
        modifier,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Meetings.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.Welcome.route) {
                WelcomeScreen(navController)
            }
            composable(NavRoutes.Auth.route) {
                AuthScreen(navController)
            }
            composable(NavRoutes.Registration.route) {
                RegistrationScreen(navController)
            }
            composable(NavRoutes.Meetings.route) {
                MeetingsScreen(navController, modifier = Modifier.padding(10.dp))
            }
            composable(NavRoutes.Users.route) {
                UsersScreen(navController, modifier = Modifier.padding(10.dp))
            }
            composable(NavRoutes.Profile.route) {
                ProfileScreen(navController, true, modifier = Modifier.padding(10.dp))
            }
            composable(NavRoutes.CreateMeeting.route) {
                CreateMeeting(navController)
            }
            composable(NavRoutes.MeetingDetail.route) {
                Meeting(navController)
//                Text("В разработке")
            }
            composable(NavRoutes.UserDetail.route) {
                ProfileScreen(navController, false, modifier = Modifier.padding(10.dp))
            }
        }
    }
}

// Класс для хранения данных о элементах навигации
data class NavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

sealed class NavRoutes(val route: String) {
    data object Welcome : NavRoutes("welcome")
    data object Auth : NavRoutes("auth")
    data object Registration : NavRoutes("registration")
    data object Meetings : NavRoutes("meetings")
    data object Users : NavRoutes("users")
    data object Profile : NavRoutes("profile")
    data object CreateMeeting : NavRoutes("create_meeting")
    data object MeetingDetail : NavRoutes("meeting_detail")
    data object UserDetail : NavRoutes("user_detail")
}

