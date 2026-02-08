package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.White

enum class MainTab {
    Home, Calendar, Add, Profile, Settings
}

@Composable
fun MainBottomBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(60.dp))
            .background(Blue)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarIcon(
                tab = MainTab.Home,
                currentTab = currentTab,
                onTabSelected = onTabSelected,
                icon = painterResource(R.drawable.ic_home)
            )

            BottomBarIcon(
                tab = MainTab.Calendar,
                currentTab = currentTab,
                onTabSelected = onTabSelected,
                icon = painterResource(R.drawable.ic_calendar)
            )

            BottomBarIcon(
                tab = MainTab.Add,
                currentTab = currentTab,
                onTabSelected = onTabSelected,
                icon = painterResource(R.drawable.ic_add)
            )

            BottomBarIcon(
                tab = MainTab.Profile,
                currentTab = currentTab,
                onTabSelected = onTabSelected,
                icon = painterResource(R.drawable.ic_person)
            )

            BottomBarIcon(
                tab = MainTab.Settings,
                currentTab = currentTab,
                onTabSelected = onTabSelected,
                icon = painterResource(R.drawable.ic_settings)
            )
        }
    }
}

@Composable
private fun BottomBarIcon(
    tab: MainTab,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    icon: Painter
) {
    val selected = tab == currentTab
    IconButton(onClick = { onTabSelected(tab) },
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = if (selected) White else DarkBlue
        )
    }
}