package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.components.MainBottomBar
import ru.sicampus.bootcamp2026.components.MainTab
import ru.sicampus.bootcamp2026.components.MainTopBar
import ru.sicampus.bootcamp2026.data.remote.AuthStore
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun ProfileScreen(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    onOpenInvitations: () -> Unit,
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by AuthStore.user.collectAsState()
    val nameLine = listOfNotNull(user?.name, user?.lastname).joinToString(" ").ifBlank { "—" }
    val loginLine = user?.login ?: "—"
    val positionLine = user?.position ?: "—"
    val aboutLine = user?.aboutMe

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(136.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(Gray)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Профиль",
                    style = MaterialTheme.typography.titleLarge,
                    color = White
                )
                Text(
                    text = "Имя: $nameLine",
                    style = MaterialTheme.typography.bodyMedium,
                    color = White
                )
                Text(
                    text = "Логин: $loginLine",
                    style = MaterialTheme.typography.bodyMedium,
                    color = White
                )
                Text(
                    text = "Должность: $positionLine",
                    style = MaterialTheme.typography.bodyMedium,
                    color = White
                )

                if (!aboutLine.isNullOrBlank()) {
                    Text(
                        text = "О себе: ${aboutLine!!}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White
                    )
                }

                Spacer(Modifier.height(8.dp))

                val actionButtonColors = ButtonDefaults.outlinedButtonColors(
                    containerColor = White.copy(alpha = 0.12f),
                    contentColor = White
                )
                val actionButtonBorder = BorderStroke(1.dp, White.copy(alpha = 0.35f))

                OutlinedButton(
                    onClick = onOpenInvitations,
                    modifier = Modifier.fillMaxWidth(),
                    colors = actionButtonColors,
                    border = actionButtonBorder
                ) {
                    Text("Приглашения")
                }

                OutlinedButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.fillMaxWidth(),
                    colors = actionButtonColors,
                    border = actionButtonBorder
                ) {
                    Text("Настройки")
                }

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = actionButtonColors,
                    border = actionButtonBorder
                ) {
                    Text("Выйти")
                }
            }

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(96.dp))
        }

        MainTopBar(
            title = "Профиль",
            modifier = Modifier.align(Alignment.TopCenter)
        )

        MainBottomBar(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
