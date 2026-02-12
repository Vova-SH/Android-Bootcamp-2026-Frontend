package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.ui.ui_components.week_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.planify.core.ui.state.ResourceState
import com.example.planify.core.utils.weekBounds
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.TextEmptyMeetings
import com.example.planify.main.common.utils.dateForPage
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.HomeViewModel
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.WeeklySchedule
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.scrolls.ScheduleScroll
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.skeleton_meeting_card.MeetingCard
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.skeleton_meeting_card.SkeletonMeetingCard
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Locale


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeWeekView(
    viewModel: HomeViewModel,
    selectedDate: LocalDate,
    scrollPagerState: PagerState,
    initialPageBottom: Int,
    onDateSelected: (LocalDate) -> Unit,
    setMonthTitle: (String) -> Unit,
    getMeetingsInfoByDate: (LocalDate) -> List<MeetingContext>,
    onMeetingClick: (Long) -> Unit
) {
    fun pageForWeek(date: LocalDate): Int =
        ChronoUnit.WEEKS.between(
            LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
            date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        )
            .toInt()

    val colors = MaterialTheme.colorScheme

    val initialPageTop = 500
    val pagerStateTop = rememberPagerState(
        initialPage = initialPageTop,
        pageCount = { 1000 }
    )

    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(selectedDate) {
        if ((uiState.meetingsInfo as? ResourceState.Success)?.data?.contains(selectedDate) != true) {
            val (start, end) = selectedDate.weekBounds()
            viewModel.runFetchMeetingsInfo(start.toLocalDate(), end.toLocalDate())
        }

        snapshotFlow { selectedDate }
            .collect {
                val currentPage = pageForWeek(selectedDate)
                pagerStateTop.animateScrollToPage(initialPageTop + currentPage)
            }
    }

    @Suppress("DEPRECATION")
    fun getMonthTitle(offset: Int) = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .plusWeeks(offset.toLong())
        .format(ofPattern("LLLL yyyy", Locale("ru")))
        .replaceFirstChar { it.uppercase() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WeeklySchedule(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected,
            initialPage = initialPageTop,
            pagerState = pagerStateTop,
            setMonthTitle = {
                setMonthTitle(
                    getMonthTitle(it)
                )
            },
            viewModel = viewModel
        )

        PullToRefreshBox(
            isRefreshing = uiState.meetingsInfo is ResourceState.Refreshing,
            onRefresh = {
                val (start, end) = selectedDate.weekBounds()
                viewModel.runFetchMeetingsInfo(start.toLocalDate(), end.toLocalDate(), reset = true)

                viewModel.runFetchMeetingsShort(
                    start = selectedDate.minusDays(60),
                    end = selectedDate.plusDays(60),
                    reset = true
                )
            },
            state = pullRefreshState,
            modifier = Modifier.weight(1f)
        ) {
            ScheduleScroll(
                onDateSelected = onDateSelected,
                selectedDate = selectedDate,
                pagerState = scrollPagerState,
                initialPage = initialPageBottom
            ) { page ->

                val meetings = remember(selectedDate, uiState) {
                    if (uiState.meetingsInfo is ResourceState.Success) {
                        getMeetingsInfoByDate(page.dateForPage(initialPageBottom))
                    } else emptyList()
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = Locals.spacing.xs,
                        bottom = Locals.dimens.bottomBarHeight
                    ),
                    verticalArrangement = Arrangement.spacedBy(Locals.spacing.xs),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (uiState.meetingsInfo) {
                        is ResourceState.Loading, is ResourceState.Refreshing, is ResourceState.Idle -> {
                            items(3) { SkeletonMeetingCard() }
                        }

                        is ResourceState.Error -> {
                            item {
                                Text("Runtime error")
                            }
                        }

                        is ResourceState.Success -> {
                            if (meetings.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(0.8f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        TextEmptyMeetings()
                                    }
                                }
                            } else {
                                items(meetings) { info ->
                                    MeetingCard(
                                        meetingInfo = info,
                                        onClick = onMeetingClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}