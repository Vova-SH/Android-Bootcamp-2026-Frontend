package ru.sicampus.bootcamp2026.ui.screen.meetings.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.sicampus.bootcamp2026.ui.screen.meetings.calendar.month.MonthCalendar
import ru.sicampus.bootcamp2026.ui.screen.meetings.calendar.week.WeekCalendar
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun Calendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var mode  by remember { mutableStateOf(CalendarState.WEEK) }

    val monthPagerState = rememberPagerState(
        initialPage = 5000,
        pageCount = { 10000 }
    )
    val currentMonth = remember {
        LocalDate.now().withDayOfMonth(1)
    }
    LaunchedEffect(selectedDate) {
        val targetPage =
            5000 + ChronoUnit.MONTHS.between(
                currentMonth,
                selectedDate.withDayOfMonth(1)
            ).toInt()
        monthPagerState.scrollToPage(targetPage)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 290.dp)
            .padding(bottom = 24.dp)
            .zIndex(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
            .padding(horizontal = 16.dp)
    ) {
        CalendarTitle(
            pagerState = monthPagerState,
            mode = mode,
            onClick = {
                mode =
                    if (mode == CalendarState.WEEK)
                        CalendarState.MONTH
                    else
                        CalendarState.WEEK
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        WeekHeader(
            selectedDate
        )

        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                (expandVertically() + fadeIn())
                    .togetherWith(
                        shrinkVertically() + fadeOut()
                    )
                    .using(
                        SizeTransform(clip = false)
                    )
            }
        ) { targetMode ->
            when (targetMode) {
                CalendarState.WEEK -> {
                    WeekCalendar(
                        selectedDate = selectedDate,
                        onDateSelected = onDateSelected
                    )
                }

                CalendarState.MONTH -> {
                    MonthCalendar(
                        pagerState = monthPagerState,
                        selectedDate = selectedDate,
                        onDateSelected = onDateSelected
                    )
                }
            }
        }
    }
}