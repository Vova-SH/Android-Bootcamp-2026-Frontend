package ru.sicampus.bootcamp2026.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarDayCell(
    day: Int,
    isCurrentMonth: Boolean = true,
    isToday: Boolean = false,
    isSelected: Boolean = false,
    hasMeeting: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .clickable { onClick() }
            .border(
                width = if (isToday && !isSelected) 2.dp else 0.dp,
                color = MaterialTheme.colorScheme.secondary,
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = day.toString(),
                fontSize = 16.sp,
                fontWeight = if (isCurrentMonth) FontWeight.Medium else FontWeight.Normal,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                }
            )

            if (hasMeeting) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                )
            }
        }
    }
}

@Composable
fun CalendarDayCellPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp)
        ) {
            CalendarDayCell(
                day = 15,
                isToday = true,
                hasMeeting = true,
                onClick = {}
            )
        }
    }
}