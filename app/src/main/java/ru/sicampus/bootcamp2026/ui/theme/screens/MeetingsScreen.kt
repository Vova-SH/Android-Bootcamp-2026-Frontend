package ru.sicampus.bootcamp2026.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun InviteScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigation(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Header()
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Требуют ответа",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(12.dp))
            InviteCard(
                title = "Рабочая встреча №1",
                date = "30 января 2026",
                time = "11:00",
                members = "4 участника",
                confirm = true
            )
            Spacer(Modifier.height(12.dp))
            InviteCard(
                title = "Рабочая встреча №2",
                date = "10 февраля 2026",
                time = "14:00",
                members = "4 участника",
                confirm = false
            )
            Spacer(Modifier.height(12.dp))
            InviteCard(
                title = "Обсуждение дизайна",
                date = "12 февраля 2026",
                time = "16:00",
                members = "3 участника",
                confirm = true
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Приглашения",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "У вас 3 новых приглашения",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InviteCard(
    title: String,
    date: String,
    time: String,
    members: String,
    confirm: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(date)
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(time)
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(members)
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (confirm) {
                    ActionButton(
                        text = "Подтвердить",
                        color = Color(0xFF33C75A),
                        icon = Icons.Default.Check,
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        text = "Отклонить",
                        color = Color(0xFFE53935),
                        icon = Icons.Default.Close,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    ActionButton(
                        text = "Отклонить",
                        color = Color(0xFFE53935),
                        icon = Icons.Default.Close,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(100.dp)
    ) {
        Icon(icon, null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("main")
            },
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("Расписание") }
        )
        NavigationBarItem(
            selected = true,
            onClick = {
                navController.navigate("meetings")
            },
            icon = { Icon(Icons.Default.Email, null) },
            label = { Text("Приглашения") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("profile")
            },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Профиль") }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun InvitePreview() {
    val navController = rememberNavController()
    MaterialTheme {
        InviteScreen(navController)
    }
}
