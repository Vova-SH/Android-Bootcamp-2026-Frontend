package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.scrolls

import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.planify.main.common.utils.dateForPage
import com.example.planify.main.common.utils.pageForDate
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate

@Composable
fun ScheduleScroll(
    onDateSelected: (LocalDate) -> Unit,
    selectedDate: LocalDate,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    initialPage: Int,
    pageContent: @Composable (PagerScope.(Int) -> Unit)
) {
    val latestSelectedDate = rememberUpdatedState(selectedDate)
    val latestOnDateSelected = rememberUpdatedState(onDateSelected)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                val newDate = page.dateForPage(initialPage)
                if (newDate != latestSelectedDate.value) {
                    latestOnDateSelected.value(newDate)
                }
            }
    }

    LaunchedEffect(selectedDate) {
        val target = selectedDate.pageForDate(initialPage)
        Log.d(
            "SCHEDULE SCROLL", "target: $target, current page: ${pagerState.currentPage}, selected date: $selectedDate")
        if (target != pagerState.currentPage) {
            pagerState.scrollToPage(target)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        beyondViewportPageCount = 1
        ) { page ->
        pageContent(page)
    }
}