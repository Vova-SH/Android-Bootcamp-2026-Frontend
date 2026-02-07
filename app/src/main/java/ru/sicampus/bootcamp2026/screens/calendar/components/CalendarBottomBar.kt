package ru.sicampus.bootcamp2026.screens.calendar.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomBar(
    onEditMeetingsClick: () -> Unit = {},
    onAddMeetingClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier,
        actions = {


            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Редактирование встреч",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )

            IconButton(onClick = onEditMeetingsClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать встречи"
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Календарь"
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMeetingClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить встречу"
                )
            }
        }
    )
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Редактирование встреч",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Редактировать",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CalendarBottomBarPreview() {
    MaterialTheme {
        CalendarBottomBar()
    }
}