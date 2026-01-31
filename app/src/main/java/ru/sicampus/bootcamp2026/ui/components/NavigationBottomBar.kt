package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeBottomBar(
    currentTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(64.dp),
            shape = RoundedCornerShape(60.dp),
            color = Color(0xFF2A2A2A).copy(alpha = 0.7f),
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Список прошедших встреч (Слева)
                BottomBarItem(
                    icon = Icons.Default.Description,
                    isSelected = currentTab == 0,
                    onClick = { onTabSelected(0) }
                )

                // Home (Центр)
                BottomBarItem(
                    icon = Icons.Default.Home,
                    isSelected = currentTab == 1,
                    onClick = { onTabSelected(1) }
                )

                // Приглашения (Справа)
                BottomBarItem(
                    icon = Icons.Default.Notifications,
                    isSelected = currentTab == 2,
                    onClick = { onTabSelected(2) }
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun NavigationBottomBarPreview() {
    HomeBottomBar(currentTab = 1, onTabSelected = {})
}