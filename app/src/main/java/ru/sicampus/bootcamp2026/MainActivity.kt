package ru.sicampus.bootcamp2026

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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.data.MeetingData
import ru.sicampus.bootcamp2026.data.UserData
import ru.sicampus.bootcamp2026.screen.CreateMeeting
import ru.sicampus.bootcamp2026.screen.Meeting
import ru.sicampus.bootcamp2026.screen.main.MeetingsScreen
import ru.sicampus.bootcamp2026.screen.main.ProfileScreen
import ru.sicampus.bootcamp2026.screen.main.UsersScreen
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import java.time.LocalDateTime

var currentUser: UserData? = null
var selectedUser: UserData? = null
var selectedMeeting : MeetingData? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme {
                val navController = rememberNavController()

                val user2 = UserData(
                    surname = "Петрова",
                    name = "Мария",
                    patronymic = "Сергеевна",
                    telephone = "+79117654321",
                    mail = "petrova@example.com"
                )

                val user3 = UserData(
                    surname = "Сидоров",
                    name = "Алексей",
                    patronymic = null,
                    telephone = "+79119876543",
                    mail = "sidorov@example.com"
                )

                val user4 = UserData(
                    surname = "Козлова",
                    name = "Анна",
                    patronymic = "Дмитриевна",
                    telephone = "+79115556677",
                    mail = "kozlova@example.com"
                )
                currentUser = UserData(
                    surname = "Иванов",
                    name = "Иван",
                    patronymic = "Иванович",
                    telephone = "+79111234567",
                    mail = "ivanov@example.com"
                )
                currentUser?.let { currentUser ->

                    currentUser.friends.add(
                        user2
                    )
                    currentUser.friends.add(
                        user4
                    )

                    currentUser.contacts["telegram"] = "@asfasff"
                    currentUser.contacts["vk"] = "safafa"
                    currentUser.contacts["gsggs"] = "sdasg"

                    currentUser.avatar = ImageBitmap.imageResource(R.drawable.img_1);
                }

                NavBar(
                    Modifier
                        .fillMaxSize(),
                    navController,
                    listOf(user2, user3, user4),
                    mutableListOf(
                        MeetingData(
                            name = "Еженедельный планёрка",
                            description = "Обсуждение текущих задач и планов на неделю",
                            time = LocalDateTime.of(2024, 10, 28, 10, 0),
                            users = mutableMapOf(
                                currentUser!! to MeetingData.InvitationState.Agree,
                                user2 to MeetingData.InvitationState.NoAnswer,
                                user3 to MeetingData.InvitationState.Disagree
                            ),
                            admins = mutableListOf(currentUser!!),
                            creator = currentUser!!
                        ),
                        MeetingData(
                            name = "Брейншторм по новому проекту",
                            description = "Генерация идей для мобильного приложения",
                            time = LocalDateTime.of(2024, 11, 5, 14, 30),
                            users = mutableMapOf(
                                user2 to MeetingData.InvitationState.Agree,
                                user4 to MeetingData.InvitationState.Agree
                            ),
                            admins = mutableListOf(user2, user4),
                            creator = user2
                        ),
                        MeetingData(
                            name = "Корпоративное обучение",
                            description = "Воркшоп по Kotlin Multiplatform",
                            time = LocalDateTime.of(2024, 11, 12, 9, 0),
                            users = mutableMapOf(
                                currentUser!! to MeetingData.InvitationState.NoAnswer,
                                user2 to MeetingData.InvitationState.Agree,
                                user3 to MeetingData.InvitationState.Agree,
                                user4 to MeetingData.InvitationState.NoAnswer
                            ),
                            admins = mutableListOf(user3),
                            creator = user3
                        ),
                        MeetingData(
                            name = "Собеседование с кандидатом",
                            description = "Интервью на позицию Android-разработчика",
                            time = LocalDateTime.of(2024, 11, 3, 16, 45),
                            users = mutableMapOf(
                                currentUser!! to MeetingData.InvitationState.Agree,
                                user3 to MeetingData.InvitationState.Agree
                            ),
                            admins = mutableListOf(currentUser!!, user3),
                            creator = currentUser!!
                        )
                    )
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
    users: List<UserData>,
    meetings: List<MeetingData>,
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
            composable(NavRoutes.Meetings.route) {
                MeetingsScreen(navController, meetings = meetings, modifier = Modifier.padding(10.dp))
            }
            composable(NavRoutes.Users.route) {
                UsersScreen(navController, users, modifier = Modifier.padding(10.dp))
            }
            composable(NavRoutes.Profile.route) {
                ProfileScreen(navController, true, modifier = Modifier.padding(10.dp))
            }
            composable(NavRoutes.CreateMeeting.route) {
                CreateMeeting(navController)
            }
            composable(NavRoutes.MeetingDetail.route) {
                Meeting(navController)
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
    data object Meetings : NavRoutes("meetings")
    data object Users : NavRoutes("users")
    data object Profile : NavRoutes("profile")
    data object CreateMeeting : NavRoutes("create_meeting")
    data object MeetingDetail : NavRoutes("meeting_detail/{meetingId}")
    data object UserDetail : NavRoutes("user_detail/{userId}")
}

@Preview
@Composable
fun MainPreview() {
    val navController = rememberNavController()

    val user2 = UserData(
        surname = "Петрова",
        name = "Мария",
        patronymic = "Сергеевна",
        telephone = "+79117654321",
        mail = "petrova@example.com"
    )

    val user3 = UserData(
        surname = "Сидоров",
        name = "Алексей",
        patronymic = null,
        telephone = "+79119876543",
        mail = "sidorov@example.com"
    )

    val user4 = UserData(
        surname = "Козлова",
        name = "Анна",
        patronymic = "Дмитриевна",
        telephone = "+79115556677",
        mail = "kozlova@example.com"
    )
    currentUser = UserData(
        surname = "Иванов",
        name = "Иван",
        patronymic = "Иванович",
        telephone = "+79111234567",
        mail = "ivanov@example.com"
    )
    currentUser?.let { currentUser ->

        currentUser.friends.add(
            user2
        )
        currentUser.friends.add(
            user4
        )

        currentUser.contacts["telegram"] = "@asfasff"
        currentUser.contacts["vk"] = "safafa"
        currentUser.contacts["gsggs"] = "sdasg"

        currentUser.avatar = ImageBitmap.imageResource(R.drawable.img_1);
    }

    NavBar(
        Modifier
            .fillMaxSize(),
        navController,
        listOf(user2, user3, user4),
        mutableListOf(
            MeetingData(
                name = "Еженедельный планёрка",
                description = "Обсуждение текущих задач и планов на неделю",
                time = LocalDateTime.of(2024, 10, 28, 10, 0),
                users = mutableMapOf(
                    currentUser!! to MeetingData.InvitationState.Agree,
                    user2 to MeetingData.InvitationState.NoAnswer,
                    user3 to MeetingData.InvitationState.Disagree
                ),
                admins = mutableListOf(currentUser!!),
                creator = currentUser!!
            ),
            MeetingData(
                name = "Брейншторм по новому проекту",
                description = "Генерация идей для мобильного приложения",
                time = LocalDateTime.of(2024, 11, 5, 14, 30),
                users = mutableMapOf(
                    user2 to MeetingData.InvitationState.Agree,
                    user4 to MeetingData.InvitationState.Agree
                ),
                admins = mutableListOf(user2, user4),
                creator = user2
            ),
            MeetingData(
                name = "Корпоративное обучение",
                description = "Воркшоп по Kotlin Multiplatform",
                time = LocalDateTime.of(2024, 11, 12, 9, 0),
                users = mutableMapOf(
                    currentUser!! to MeetingData.InvitationState.NoAnswer,
                    user2 to MeetingData.InvitationState.Agree,
                    user3 to MeetingData.InvitationState.Agree,
                    user4 to MeetingData.InvitationState.NoAnswer
                ),
                admins = mutableListOf(user3),
                creator = user3
            ),
            MeetingData(
                name = "Собеседование с кандидатом",
                description = "Интервью на позицию Android-разработчика",
                time = LocalDateTime.of(2024, 11, 3, 16, 45),
                users = mutableMapOf(
                    currentUser!! to MeetingData.InvitationState.Agree,
                    user3 to MeetingData.InvitationState.Agree
                ),
                admins = mutableListOf(currentUser!!, user3),
                creator = currentUser!!
            )
        )
    )
}
