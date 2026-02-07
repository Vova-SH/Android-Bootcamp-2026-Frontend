package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.data.model.MeetingDto
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.theme.PrimaryPurple
import ru.sicampus.bootcamp2026.ui.theme.TextWhite
import ru.sicampus.bootcamp2026.ui.theme.getMeetingColor
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder
import ru.sicampus.bootcamp2026.ui.utils.darken
import ru.sicampus.bootcamp2026.ui.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun MainScreen(
    onAddMeetingClicked: () -> Unit,
    onInvitesClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val meetings by viewModel.meetings.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val avatarUrl by viewModel.avatarUrl.collectAsState()

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
            HeaderSection(onInvitesClicked, onProfileClicked, avatarUrl)
            Spacer(modifier = Modifier.height(16.dp))

            DaysSelectorSection(
                selectedDate = selectedDate,
                onDateSelected = { viewModel.selectDate(it) }
            )

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
fun DaysSelectorSection(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val days = (0..6).map { LocalDate.now().plusDays(it.toLong()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        days.forEach { date ->
            val isSelected = date == selectedDate
            DayCard(
                date = date,
                isSelected = isSelected,
                onClick = { onDateSelected(date) }
            )
        }
    }
}

@Composable
fun DayCard(date: LocalDate, isSelected: Boolean, onClick: () -> Unit) {
    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")).uppercase()
    val dayNumber = date.dayOfMonth.toString()

    Column(
        modifier = Modifier
            .width(70.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryPurple else Color(0xFFF3F4F6))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = dayName,
            fontSize = 12.sp,
            color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.5f)
        )
        Text(
            text = dayNumber,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Black
        )
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

            val meetingAtThisHour = meetings.find {
                try {
                    it.startsAt?.let { utcString ->
                        val localTime = OffsetDateTime.parse(utcString)
                            .atZoneSameInstant(ZoneId.systemDefault())
                        localTime.hour == hour
                    } ?: false
                } catch (e: Exception) { false }
            }

            TimeSlotRow(time = timeString, meeting = meetingAtThisHour)
        }
        Spacer(modifier = Modifier.height(80.dp))
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

            if (meeting != null) {
                val cardColor = getMeetingColor(meeting.colorHex)
                val accentColor = cardColor.darken(0.3f)
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier.fillMaxSize().padding(1.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
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
                                text = meeting.title ?: "Встреча",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            if (!meeting.description.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = meeting.description,
                                    color = Color.Black.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(onInvitesClicked: () -> Unit, onProfileClicked: () -> Unit, avatarUrl: String?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Мое расписание", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Row {
            IconButton(onClick = onInvitesClicked) {
                Icon(Icons.Outlined.Notifications, null, tint = Color.Black)
            }
            IconButton(onClick = onProfileClicked) {
                if (!avatarUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(32.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(Modifier.size(32.dp).clip(CircleShape).background(Color.LightGray))
                }
            }
        }
    }
}