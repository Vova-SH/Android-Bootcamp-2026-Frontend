package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planify.core.ui.state.ResourceState
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.HomeViewModel
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.entities.getWeekDays
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklySchedule(
    viewModel: HomeViewModel,
    selectedDate: LocalDate,
    pagerState: PagerState,
    initialPage: Int,
    setMonthTitle: (Int) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                setMonthTitle(page - initialPage)
            }
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth(),
                beyondViewportPageCount = 1
            ) { page ->
                if ((uiState.meetingsInfoShort as? ResourceState.Success)?.data?.contains(selectedDate) != true) {
                    val start = selectedDate.minusDays(60)
                    val end = selectedDate.plusDays(60)
                    viewModel.runFetchMeetingsShort(start, end)
                }

                val weekDays = remember(page) { getWeekDays(page - initialPage) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    weekDays.forEach { day ->
                        DayCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(Locals.dimens.dayCardHeight),
                            viewModel = viewModel,
                            day = day,
                            isSelected = day.date == selectedDate,
                            onClick = { onDateSelected(day.date) }
                        )
                    }
                }
            }
        }
    }
}
