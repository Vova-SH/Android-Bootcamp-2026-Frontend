package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.theme.PrimaryPurple
import ru.sicampus.bootcamp2026.ui.theme.TextWhite
import ru.sicampus.bootcamp2026.data.model.MeetingDto
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder
import ru.sicampus.bootcamp2026.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    onAddMeetingClicked: () -> Unit,
    onInvitesClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val meetings by viewModel.meetings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    MainScreenContent(
        isLoading = isLoading,
        meetings = meetings,
        onAddMeetingClicked = onAddMeetingClicked,
        onInvitesClicked = onInvitesClicked,
        onProfileClicked = onProfileClicked
    )
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
fun MainScreenContent(
    isLoading: Boolean,
    meetings: List<MeetingDto>,
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
            HeaderSection(onInvitesClicked, onProfileClicked)
            Spacer(modifier = Modifier.height(16.dp))
            DaysSelectorSection()
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryPurple)
                }
            } else {
                ScheduleGridSection(meetings = meetings)
            }
        }
    }
}

@Composable
fun ScheduleGridSection(meetings: List<MeetingDto>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        for (hour in 9..21) {
            val timeString = String.format("%02d:00", hour)
            val meetingAtThisHour = meetings.find { it.startTime.startsWith(String.format("%02d", hour)) }
            TimeSlotRow(time = timeString, meeting = meetingAtThisHour)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun TimeSlotRow(time: String, meeting: MeetingDto?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .heightIn(min = 60.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = time,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Color.Black.copy(alpha = 0.5f),
            modifier = Modifier
                .width(45.dp)
                .padding(top = 8.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .customDashedBorder()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                if (meeting != null) {
                    val cardColor = parseColor(meeting.colorHex) ?: PrimaryPurple.copy(alpha = 0.1f)
                    val accentColor = parseColor(meeting.colorHex)?.copy(alpha = 1f)?.darken(0.3f) ?: PrimaryPurple

                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(0.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(IntrinsicSize.Min)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(accentColor)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = meeting.title,
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                )

                                if (!meeting.description.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = meeting.description,
                                        color = Color.Black.copy(alpha = 0.6f),
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun parseColor(hex: String?): Color? {
    return try {
        if (hex.isNullOrEmpty()) null else Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        null
    }
}

fun Color.darken(factor: Float): Color {
    return Color(
        red = (this.red * (1 - factor)).coerceAtLeast(0f),
        green = (this.green * (1 - factor)).coerceAtLeast(0f),
        blue = (this.blue * (1 - factor)).coerceAtLeast(0f),
        alpha = this.alpha
    )
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
            .clickable { /* TODO */ },
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MainScreenPreview() {
    val mockMeetings = listOf(
        MeetingDto(1, "Дейли", "10:00", "11:00", "Тест", "#F3E8FF"),
        MeetingDto(2, "Обсуждение API", "14:00", "15:00", "Тест", "#E0F2FE")
    )

    MainScreenContent(
        isLoading = false,
        meetings = mockMeetings,
        onAddMeetingClicked = {},
        onInvitesClicked = {},
        onProfileClicked = {}
    )
}