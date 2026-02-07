package ru.sicampus.bootcamp2026.screens.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.screens.calendar.components.CalendarGrid
import ru.sicampus.bootcamp2026.screens.calendar.components.CalendarHeader

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onEditMeetingsClick: () -> Unit = {},
    onAddMeetingClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onNavigationItemClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableIntStateOf(0) }

    val bottomNavItems = listOf(
        BottomNavItem("Главная", Icons.Default.Home, "home"),
        BottomNavItem("Календарь", Icons.Default.CalendarToday, "calendar"),
        BottomNavItem("Встречи", Icons.Default.Event, "meetings"),
        BottomNavItem("Профиль", Icons.Default.Person, "profile"),
    )

    Scaffold(
        topBar = {
            CalendarHeader()
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            onNavigationItemClick(item.route)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                CalendarGrid()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CalendarScreen(
                onEditMeetingsClick = { /* TODO */ },
                onAddMeetingClick = { /* TODO */ },
                onMenuClick = { /* TODO */ },
                onNavigationItemClick = { route ->
                    println("Нажата навигация: $route")
                }
            )
        }
    }
}