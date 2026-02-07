package com.example.meet.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meet.R
import com.example.meet.ui.theme.MeetTheme

@Composable
fun DailyMeetingsScreen(
    onCreateMeetingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val currentDate = remember { mutableStateOf(java.time.LocalDate.of(2026, 1, 23)) }
    val showCreateButton by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        // === Верхняя панель: дата и стрелки ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back), // ←
                contentDescription = "Вчера",
                modifier = Modifier.clickable {
                    currentDate.value = currentDate.value.minusDays(1)
                },
                tint = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = currentDate.value.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward), // →
                contentDescription = "Завтра",
                modifier = Modifier.clickable {
                    currentDate.value = currentDate.value.plusDays(1)
                },
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // === Список встреч ===
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp, bottom = 80.dp) // отступ под нижнюю панель и верхнюю
                .verticalScroll(rememberScrollState())
        ) {
            // Пример встречи 1
            MeetingItem(
                time = "7:00–8:00",
                title = "Встреча с генеральным директором",
                location = "Место: конференц зал",
                details = "Подробности: взять готовые проекты."
            )

            // Пример встречи 2
            MeetingItem(
                time = "15:00–16:00",
                title = "Встреча с тимлидом",
                location = "Место: зал A1",
                details = "Подробности: взять ноутбуки."
            )


        }

        // === Нижняя навигационная панель ===
        BottomAppBar(
            modifier = Modifier.align(Alignment.BottomStart),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                NavIcon(
                    iconId = R.drawable.ic_calendar,
                    label = "Календарь",
                    isSelected = true
                )
                NavIcon(
                    iconId = R.drawable.ic_bell,
                    label = "Уведомления",
                    isSelected = false
                )
                NavIcon(
                    iconId = R.drawable.ic_person,
                    label = "Профиль",
                    isSelected = false
                )
            }
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(y = (-72).dp),
            horizontalAlignment = Alignment.End
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(expandFrom = Alignment.Bottom),
                exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
            ) {
                Button(
                    onClick = {
                        onCreateMeetingClick()
                        isExpanded = false
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("Создать встречу", fontSize = 14.sp)
                }
            }

            FloatingActionButton(
                onClick = { isExpanded = !isExpanded },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "Добавить"
                )
            }
        }
    }
}

// --- Вспомогательные компоненты ---

@Composable
private fun MeetingItem(
    time: String,
    title: String,
    location: String,
    details: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = location,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )
            )
            Text(
                text = details,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            )
        }
    }
}

@Composable
private fun NavIcon(
    iconId: Int,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .clickable { /* можно добавить навигацию */ }
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp
            ),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DailyMeetingsScreenPreview() {
    MeetTheme {
        DailyMeetingsScreen()
    }
}