package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.scrolls

import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun MonthScroll(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    pagerState: PagerState,
    initialPage: Int,
    pageContent: @Composable (PagerScope.(Int) -> Unit)
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {

    }

    LaunchedEffect(selectedDate) {
        snapshotFlow { selectedDate }
            .distinctUntilChanged()
            .collect {
                val target = initialPage + ChronoUnit.MONTHS
                    .between(LocalDate.now().withDayOfMonth(1),
                        selectedDate.withDayOfMonth(1))
                    .toInt()

                if (pagerState.currentPage != target) {
                    pagerState.animateScrollToPage(target)
                }

                Log.d(
                    "MONTH SCROLL", "target: $target, current page: ${pagerState.currentPage}, selected date: $selectedDate"
                )

            }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        beyondViewportPageCount = 1
    ) { page ->  // TODO: Load dots when switching month
        pageContent(page)
    }
}