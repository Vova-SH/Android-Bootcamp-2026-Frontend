package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.theme.PrimaryPurple
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder

data class Participant(
    val id: Int,
    val name: String
)

@Composable
fun NewMeetingScreen(onBackClicked: () -> Unit) {

    val participants = remember {
        listOf(
            Participant(1, "Морозов Михаил Андреевич"),
            Participant(2, "Кузнецов Анатолий Васильевич"),
            Participant(3, "Соколова Екатерина Павловна"),
            Participant(4, "Авдеева Анастасия Петровна"),
            Participant(5, "Агапов Максим Алексеевич"),
            Participant(6, "Попов Владимир Ильич"),
            Participant(7, "Васильев Андрей Петрович"),
            Participant(8, "Александров Дмитрий Николаевич"),
            Participant(9, "Смирнов Алексей Владимирович"),
            Participant(10, "Иванова Мария Сергеевна"),
        )
    }

    var selectedIds by remember { mutableStateOf(setOf<Int>()) }
    var selectedTime by remember { mutableStateOf("11:00 - 12:00") }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopBar(onBackClicked)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                Button(
                    onClick = { /* TODO: Логика создания */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "Создать",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            FieldLabel("Тема")
            DashedField("Общий дневной созвон")

            FieldLabel("Дата")
            DashedFieldWithIcon(
                text = "30 января 2026, Пятница",
                icon = Icons.Default.DateRange
            )

            FieldLabel("Время")
            TimeSelector(
                selected = selectedTime,
                onSelect = { selectedTime = it }
            )

            FieldLabel("Участники")
            SearchField()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .customDashedBorder()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Transparent)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(participants) { p ->
                        ParticipantRow(
                            participant = p,
                            checked = selectedIds.contains(p.id),
                            onToggle = {
                                selectedIds =
                                    if (selectedIds.contains(p.id)) selectedIds - p.id
                                    else selectedIds + p.id
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onBackClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Новая встреча",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = BackgroundColor
        )
    )
}

@Composable
fun FieldLabel(text: String) {
    Text(
        text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black.copy(alpha = 0.8f)
    )
}

@Composable
fun DashedField(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .customDashedBorder()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun DashedFieldWithIcon(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .customDashedBorder()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )
        Icon(
            icon,
            contentDescription = null
        )
    }
}

@Composable
fun TimeSelector(selected: String, onSelect: (String) -> Unit) {
    val times = listOf(
        "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 13:00",
        "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00",
        "17:00 - 18:00", "18:00 - 19:00", "19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00"
    )

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.width(3.dp))

        times.forEach { time ->
            val isSelected = time == selected
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) PrimaryPurple else Color(0xFFF3F4F6))
                    .clickable { onSelect(time) }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = time,
                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun SearchField() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .customDashedBorder()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            "Кого позвать?",
            color = Color.Black.copy(alpha = 0.5f),
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Outlined.Search,
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
    }
}

@Composable
fun ParticipantRow(
    participant: Participant,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                participant.name.first().toString(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            participant.name,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            lineHeight = 18.sp
        )

        Checkbox(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryPurple
            )
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun NewMeetingScreenPreview() {
    NewMeetingScreen(onBackClicked = {})
}