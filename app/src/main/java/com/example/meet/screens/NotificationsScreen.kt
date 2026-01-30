package com.example.meet.screens.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meet.screens.main.DailyMeetingsScreen
import com.example.meet.ui.theme.MeetTheme

// Данные для демонстрации
data class Notification(
    val id: Int,
    val title: String,
    val description: String,
    val timestamp: String,
    val status: Status // NEW, ACCEPTED, REJECTED
)

enum class Status { NEW, ACCEPTED, REJECTED }

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier
) {
    val notifications by remember {
        mutableStateOf(
            listOf(
                Notification(1, "Встреча с директором", "Завтра, 10:00", "10 мин назад", Status.NEW),
                Notification(2, "Приглашение на совещание", "Сегодня, 15:30", "1 час назад", Status.ACCEPTED),
                Notification(3, "Запрос на перенос встречи", "Вчера, 9:00", "2 дня назад", Status.REJECTED),
                Notification(4, "Напоминание: подготовка отчёта", "Сегодня, 18:00", "3 часа назад", Status.NEW),
            )
        )
    }

    val selectedFilter by remember { mutableStateOf(Filter.ALL) }
    var showFilters by remember { mutableStateOf(false) }

    val newCount = notifications.count { it.status == Status.NEW }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            // === Заголовок ===
            Text(
                text = "Уведомления",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(16.dp)
            )

            // === Кнопка выбора фильтров ===
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { showFilters = !showFilters },
                    modifier = Modifier.height(36.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Фильтры",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Выбор", fontSize = 14.sp)
                }
            }

            // === Фильтры (раскрываются) ===
            AnimatedVisibility(
                visible = showFilters,
                enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.slideInVertically { -it },
                exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.slideOutVertically { it }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    FilterButton(
                        label = "Новые",
                        isSelected = selectedFilter == Filter.NEW,
                        onClick = { /* обновить фильтр */ },
                        badgeCount = newCount,
                        modifier = Modifier.fillMaxWidth()
                    )
                    FilterButton(
                        label = "Принятые",
                        isSelected = selectedFilter == Filter.ACCEPTED,
                        onClick = { /* обновить фильтр */ },
                        badgeCount = 0,
                        modifier = Modifier.fillMaxWidth()
                    )
                    FilterButton(
                        label = "Отклонённые",
                        isSelected = selectedFilter == Filter.REJECTED,
                        onClick = { /* обновить фильтр */ },
                        badgeCount = 0,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // === Список уведомлений ===
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(notifications) { item ->
                    NotificationItem(item)
                }
            }
        }

// === Нижняя панель ===
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

// --- Вспомогательные компоненты ---

@Composable
private fun FilterButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(40.dp),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Red, CircleShape)
                        .padding(2.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = badgeCount.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = notification.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = notification.timestamp,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


enum class Filter { ALL, NEW, ACCEPTED, REJECTED }

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    MeetTheme {
        NotificationsScreen()
    }
}