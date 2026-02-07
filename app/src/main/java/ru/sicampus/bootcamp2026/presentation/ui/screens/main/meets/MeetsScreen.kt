package ru.sicampus.bootcamp2026.presentation.ui.screens.main.meets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

enum class MeetsTab {
    CREATED,
    INVITED
}

data class Meet(
    val id: String,
    val title: String,
    val time: String,
    val date: String,
    val organizer: String
)

@Composable
fun MeetsScreen() {
    MeetsContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeetsContent() {
    var selectedTab by remember { mutableStateOf(MeetsTab.CREATED) }

    val createdMeets = listOf(
        Meet("1", "Встреча с командой", "10:00", "07.10", "Вы"),
        Meet("2", "Согласование технического задания", "14:00", "30.01", "Вы")
    )

    val invitedMeets = listOf(
        Meet("3", "Обсуждение требований", "00:00", "01.01", "Никита Кузьмин"),
        Meet("4", "Синхронизация по срокам", "00:00", "01.01", "Иван Иванов")
    )

    val currentMeets = when (selectedTab) {
        MeetsTab.CREATED -> createdMeets
        MeetsTab.INVITED -> invitedMeets
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Мои встречи",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = { },
            divider = { }
        ) {
            Tab(
                selected = selectedTab == MeetsTab.CREATED,
                onClick = { selectedTab = MeetsTab.CREATED },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (selectedTab == MeetsTab.CREATED)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceContainerLowest
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (selectedTab == MeetsTab.CREATED) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text("Созданные", style = MaterialTheme.typography.labelLarge)
                }
            }

            Tab(
                selected = selectedTab == MeetsTab.INVITED,
                onClick = { selectedTab = MeetsTab.INVITED },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (selectedTab == MeetsTab.INVITED)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceContainerLowest
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (selectedTab == MeetsTab.INVITED) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text("Приглашения", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(currentMeets) { meet ->
                MeetCard(
                    meet = meet,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun MeetCard(
    meet: Meet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = meet.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${meet.time} ${meet.date}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = meet.organizer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MeetsScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        MeetsContent()
    }
}