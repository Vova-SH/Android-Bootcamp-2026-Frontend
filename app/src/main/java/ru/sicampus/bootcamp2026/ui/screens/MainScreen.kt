package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.theme.PrimaryPurple
import ru.sicampus.bootcamp2026.ui.theme.TextWhite
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder

@Composable
fun MainScreen(
    onAddMeetingClicked: () -> Unit,
    onInvitesClicked: () -> Unit,
    onProfileClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMeetingClicked,
                containerColor = PrimaryPurple,
                contentColor = TextWhite,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add meeting")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            HeaderSection(
                onInvitesClicked = onInvitesClicked,
                onProfileClicked = onProfileClicked
            )
            Spacer(modifier = Modifier.height(16.dp))
            DaysSelectorSection()
            Spacer(modifier = Modifier.height(16.dp))
            ScheduleGridSection()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {

        composable("main_screen") {
            MainScreen(
                onAddMeetingClicked = {
                    navController.navigate("new_meeting_screen")
                },
                onInvitesClicked = {
                    navController.navigate("invites_screen")
                },
                onProfileClicked = {
                    navController.navigate("profile_screen")
                }
            )
        }

        composable("new_meeting_screen") {
            NewMeetingScreen(
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable("invites_screen") {
            InvitesScreen(
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable("profile_screen") {
            ProfileScreen(
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun HeaderSection(
    onInvitesClicked: () -> Unit,
    onProfileClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {

        Row(
            modifier = Modifier.height(50.dp).weight(1f).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        ) {

            Text(
                text = "Мое расписание",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Row(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .clip(CircleShape)
                    .clickable { onInvitesClicked() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable { onProfileClicked() }
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("И", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DaysSelectorSection() {
    val days = listOf(
        "ПН" to "26",
        "ВТ" to "27",
        "СР" to "28",
        "ЧТ" to "29",
        "ПТ" to "30"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        days.forEach { (dayName, date) ->
            val isSelected = date == "28"
            DayCard(dayName, date, isSelected)
        }
    }
}

@Composable
fun DayCard(dayName: String, date: String, isSelected: Boolean) {
    Column(
        modifier = Modifier
            .width(70.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) PrimaryPurple else Color(0xFFF3F4F6)
            )
            .clickable { /* TODO: Выбор дня */ },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = dayName,
            fontSize = 14.sp,
            color = if (isSelected) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.5f)
        )
        Text(
            text = date,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
fun ScheduleGridSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        for (hour in 9..21) {
            val time = String.format("%02d:00", hour)
            TimeSlotRow(time = time)
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun TimeSlotRow(time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = time,
            fontSize = 14.sp,
            fontWeight = SemiBold,
            textAlign = TextAlign.Center,
            color = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier
                .width(50.dp)
                .align(Alignment.CenterVertically)
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .customDashedBorder()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        ){
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(1.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onAddMeetingClicked = {},
        onInvitesClicked = {},
        onProfileClicked = {}
    )
}