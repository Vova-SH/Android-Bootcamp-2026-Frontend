package ru.sicampus.bootcamp2026.presentation.ui.screens.main.invites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

data class Invite(
    val title: String,
    val time: String,
    val date: String,
    val author: String
)

@Composable
fun InvitesScreen() {
    val invites = listOf(
        Invite(
            title = "Обсуждение требований",
            time = "00:00",
            date = "01.01",
            author = "Иван Иванов"
        ),
        Invite(
            title = "Планирование тестирования",
            time = "9:20",
            date = "5 апр.",
            author = "Иван Лебедев"
        ),
        Invite(
            title = "Синхронизация по срокам",
            time = "00:00",
            date = "01.01",
            author = "Никита Ардашев"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Приглашения",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(invites) { invite ->
                MeetInvite(
                    title = invite.title,
                    time = invite.time,
                    date = invite.date,
                    author = invite.author,
                    onAccept = { /* TODO: Принять приглашение */ },
                    onDecline = { /* TODO: Отклонить приглашение */ }
                )
            }
        }
    }
}

@Composable
fun MeetInvite(
    title: String,
    time: String,
    date: String,
    author: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$time $date",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = author,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ReactButton(
                    icon = Icons.Default.Check,
                    onClick = onAccept
                )
                ReactButton(
                    icon = Icons.Default.Close,
                    onClick = onDecline
                )
            }
        }
    }
}

@Composable
fun ReactButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.size(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(
    name = "Invites Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InvitesScreenPreview() {
    AndroidBootcamp2026FrontendTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            InvitesScreen()
        }
    }
}

@Preview(
    name = "Invites Screen Dark",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InvitesScreenDarkPreview() {
    AndroidBootcamp2026FrontendTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            InvitesScreen()
        }
    }
}

@Preview
@Composable
private fun MeetInvitePreview() {
    AndroidBootcamp2026FrontendTheme(darkTheme = true) {
        MeetInvite(
            title = "Планирование тестирования",
            time = "9:20",
            date = "5 апр.",
            author = "Иван Лебедев",
            onAccept = {},
            onDecline = {}
        )
    }
}