package ru.sicampus.bootcamp2026.ui.screen.meetings

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.ui.components.*
import ru.sicampus.bootcamp2026.ui.theme.*

@Composable
fun MeetingsScreen(
    viewModel: MeetingsViewModel,
    onCreateMeetingClick: () -> Unit,
    onMeetingClick: (Long) -> Unit
) {
    val meetings by viewModel.meetings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isScheduleMode by viewModel.isScheduleMode.collectAsState()

    JuicyBackground {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onCreateMeetingClick,
                    containerColor = BrandPrimary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(28.dp))
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                    Text(
                        text = "События",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        text = "Не пропускайте важное",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextTertiary
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(SurfaceWhite, RoundedCornerShape(16.dp))
                        .padding(4.dp)
                ) {
                    Row(Modifier.fillMaxSize()) {
                        TabItem("Все", !isScheduleMode) { if(isScheduleMode) viewModel.toggleMode() }
                        TabItem("Моё расписание", isScheduleMode) { if(!isScheduleMode) viewModel.toggleMode() }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isLoading && meetings.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = BrandPrimary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        items(meetings) { meeting ->
                            HarmoniousMeetingItem(meeting, onClick = { onMeetingClick(meeting.id) })
                        }
                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) BrandPrimary else Color.Transparent
    val textColor = if (isSelected) Color.White else TextSecondary

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 15.sp),
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun HarmoniousMeetingItem(meeting: Meeting, onClick: () -> Unit) {
    JuicyCard(onClick = onClick) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateBadge(meeting.startTime)

            Spacer(Modifier.width(20.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    meeting.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.LocationOn,
                        null,
                        modifier = Modifier.size(16.dp),
                        tint = AccentPurple
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        meeting.location,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )

                    Spacer(Modifier.width(12.dp))

                    Icon(
                        Icons.Default.DateRange,
                        null,
                        modifier = Modifier.size(16.dp),
                        tint = TextTertiary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${meeting.durationMinutes} мин",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextTertiary
                    )
                }
            }
        }
    }
}

@Composable
fun DateBadge(dateString: String) {
    val parts = dateString.split("T").getOrNull(0)?.split("-")
    val day = parts?.getOrNull(2) ?: "01"
    val month = parts?.getOrNull(1) ?: "01"

    val monthName = when(month) {
        "01" -> "ЯНВ"; "02" -> "ФЕВ"; "03" -> "МАР"; "04" -> "АПР"
        "05" -> "МАЙ"; "06" -> "ИЮН"; "07" -> "ИЮЛ"; "08" -> "АВГ"
        "09" -> "СЕН"; "10" -> "ОКТ"; "11" -> "НОЯ"; "12" -> "ДЕК"
        else -> month
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(56.dp)
            .height(64.dp)
            .background(SurfaceLight, RoundedCornerShape(14.dp))
            .padding(vertical = 8.dp)
    ) {
        Text(
            day,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
            color = BrandPrimary
        )
        Text(
            monthName,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextTertiary
        )
    }
}