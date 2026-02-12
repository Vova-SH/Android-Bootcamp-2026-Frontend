package ru.sicampus.bootcamp2026.ui.screen.meetings.calendar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.PrimaryGray
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarTitle(
    pagerState: PagerState,
    mode: CalendarState,
    onClick: () -> Unit
) {
    val currentMonth = LocalDate.now().withDayOfMonth(1)
    val pagerMonth = currentMonth.plusMonths((pagerState.currentPage - 5000).toLong())


    val rotation by animateFloatAsState(
        targetValue = if (mode == CalendarState.WEEK) 0f else 90f,
        label = "rotation"
    )

    Row(
        modifier = Modifier
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = pagerMonth.month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .rotate(rotation),
            tint = PrimaryGray
        )
    }
}