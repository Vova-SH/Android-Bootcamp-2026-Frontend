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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.components.MainBottomBar
import ru.sicampus.bootcamp2026.components.MainTab
import ru.sicampus.bootcamp2026.components.MainTopBar
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun SettingsScreen(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val (notifications, setNotifications) = remember { mutableStateOf(true) }

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RowSetting(
                    title = "Уведомления",
                    value = notifications,
                    onValueChange = setNotifications
                )
            }

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(96.dp))
        }

        MainTopBar(
            title = "Настройки",
            modifier = Modifier.align(Alignment.TopCenter)
        )

        MainBottomBar(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun RowSetting(
    title: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium, color = White)
        Switch(checked = value, onCheckedChange = onValueChange)
    }
}
