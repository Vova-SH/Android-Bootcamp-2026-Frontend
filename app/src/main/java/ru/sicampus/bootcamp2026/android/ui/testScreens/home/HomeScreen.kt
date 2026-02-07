package ru.sicampus.bootcamp2026.android.ui.testScreens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.ui.components.CustomNavigationBar
import ru.sicampus.bootcamp2026.android.ui.components.MeetingCard
import ru.sicampus.bootcamp2026.android.ui.components.MeetingCardActions
import ru.sicampus.bootcamp2026.android.ui.components.MeetingUi
import ru.sicampus.bootcamp2026.android.ui.mappers.toMeetingUi
import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.android.ui.theme.IconsGrey
import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private enum class HomeViewType(val title: String) {
    WEEK("Неделя"),
    MONTH("Месяц")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNotificationsClick: () -> Unit = {},
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    var viewType by remember { mutableStateOf(HomeViewType.WEEK) }
    var showViewTypeMenu by remember { mutableStateOf(false) }

    val pendingInvitesCount by remember { mutableStateOf(2) }

    var anchorDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val state by viewModel.state.collectAsState()

    val listState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    fun scrollToTop() {
        scope.launch {
            listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(state.selectedDate) {
        listState.scrollToItem(0)
    }

    LaunchedEffect(Unit) {
        val start = startOfWeek(anchorDate)
        viewModel.loadWeek(start)
        viewModel.loadFirstPage(selectedDate)
    }

    // ПАГИНАЦИЯ
    LaunchedEffect(listState, state.selectedDate, state.isLast) {
        snapshotFlow {
            val layout = listState.layoutInfo
            val total = layout.totalItemsCount
            val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index ?: 0

            total > 0 &&
                    state.meetings.isNotEmpty() &&
                    !state.isLast &&
                    !state.isLoading &&
                    !state.isLoadingMore &&
                    lastVisible >= total - 2
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.loadNextPage()
            }
    }


    Scaffold(
        bottomBar = { CustomNavigationBar(navController = navController) }
    ) { paddingValues ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .border(1.dp, TextGrey, RoundedCornerShape(8.dp))
                                .clickable { showViewTypeMenu = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = viewType.title,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
                                color = DarkBlue
                            )
                        }

                        DropdownMenu(
                            expanded = showViewTypeMenu,
                            onDismissRequest = { showViewTypeMenu = false },
                            containerColor = Color.White,
                            tonalElevation = 4.dp,
                            shadowElevation = 8.dp
                        ) {
                            HomeViewType.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            option.title,
                                            color = DarkBlue,
                                            fontFamily = FontFamily(Font(R.font.open_sans_semibold))
                                        )
                                    },
                                    onClick = {
                                        viewType = option
                                        showViewTypeMenu = false

                                        selectedDate = anchorDate
                                        viewModel.loadFirstPage(selectedDate)

                                        if (viewType == HomeViewType.WEEK) {
                                            viewModel.loadWeek(startOfWeek(anchorDate))
                                        } else {
                                            // TODO: loadMonth потом
                                        }
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = DarkBlue,
                                        leadingIconColor = DarkBlue,
                                        trailingIconColor = DarkBlue,
                                        disabledTextColor = TextGrey
                                    )
                                )
                            }
                        }
                    }

                    IconButton(onClick = onNotificationsClick) {
                        if (pendingInvitesCount > 0) {
                            BadgedBox(badge = {
                                Badge(containerColor = Color.Red, contentColor = Color.White) {}
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = DarkBlue,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = IconsGrey,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }

            // Навигация по периоду
            item {
                PeriodNavRow(
                    viewType = viewType,
                    anchorDate = anchorDate,
                    onPrev = {
                        val newAnchor = when (viewType) {
                            HomeViewType.WEEK -> anchorDate.minusWeeks(1)
                            HomeViewType.MONTH -> anchorDate.minusMonths(1)
                        }
                        anchorDate = newAnchor

                        val newSelected = clampSelectedDate(viewType, newAnchor, selectedDate)
                        selectedDate = newSelected

                        when (viewType) {
                            HomeViewType.WEEK -> {
                                viewModel.loadWeek(startOfWeek(newAnchor))
                                viewModel.loadFirstPage(newSelected)
                                scrollToTop()
                            }
                            HomeViewType.MONTH -> {
                                // TODO: viewModel.loadMonth(YearMonth.from(newAnchor))
                                viewModel.loadFirstPage(newSelected)
                                scrollToTop()
                            }
                        }
                    },
                    onNext = {
                        val newAnchor = when (viewType) {
                            HomeViewType.WEEK -> anchorDate.plusWeeks(1)
                            HomeViewType.MONTH -> anchorDate.plusMonths(1)
                        }
                        anchorDate = newAnchor

                        val newSelected = clampSelectedDate(viewType, newAnchor, selectedDate)
                        selectedDate = newSelected

                        when (viewType) {
                            HomeViewType.WEEK -> {
                                viewModel.loadWeek(startOfWeek(newAnchor))
                                viewModel.loadFirstPage(newSelected)
                                scrollToTop()
                            }
                            HomeViewType.MONTH -> {
                                // TODO: viewModel.loadMonth(YearMonth.from(newAnchor))
                                viewModel.loadFirstPage(newSelected)
                                scrollToTop()
                            }
                        }
                    }
                )
            }

            item { Spacer(Modifier.height(12.dp)) }

            //Неделя / Месяц
            item {
                when (viewType) {
                    HomeViewType.WEEK -> {
                        val weekStart = startOfWeek(anchorDate)

                        WeekStrip(
                            weekStart = weekStart,
                            selectedDate = selectedDate,
                            meetingsCountByDate = state.weekCountsByDate,
                            onSelectDate = {
                                selectedDate = it
                                viewModel.loadFirstPage(it)
                                scrollToTop()
                            }
                        )
                    }

                    HomeViewType.MONTH -> {
                        MonthCalendar(
                            anchorDate = anchorDate,
                            selectedDate = selectedDate,
                            meetingsCountByDate = state.weekCountsByDate, // TODO:заменить на monthCounts
                            onSelectDate = { date ->
                                selectedDate = date
                                viewModel.loadFirstPage(date)
                                scrollToTop()
                            },
                            onDoubleClickDate = { date ->
                                selectedDate = date
                                anchorDate = date
                                viewType = HomeViewType.WEEK
                                viewModel.loadWeek(startOfWeek(date))
                                viewModel.loadFirstPage(date)
                                scrollToTop()
                            }
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            // список встреч (пагинация)
            items(
                items = state.meetings,
                key = { it.id }
            ) { meeting ->
                val name = state.organizerNames[meeting.organizerId] ?: "Организатор #${meeting.organizerId}"
                val ui = meeting.toMeetingUi(organizerName = name)

                MeetingCard(
                    meeting = ui,
                    actions = MeetingCardActions.None
                )
            }

            if (state.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

// Навигация влево/вправо
@Composable
private fun PeriodNavRow(
    viewType: HomeViewType,
    anchorDate: LocalDate,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    val title = remember(viewType, anchorDate) {
        when (viewType) {
            HomeViewType.WEEK -> {
                val start = startOfWeek(anchorDate)
                val end = start.plusDays(6)
                "${formatShortDate(start)} – ${formatShortDate(end)}"
            }
            HomeViewType.MONTH -> {
                val ym = YearMonth.from(anchorDate)
                "${monthNameNominative(ym.monthValue)} ${ym.year}"
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Prev", tint = DarkBlue)
        }

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
            fontSize = 16.sp,
            color = DarkBlue
        )

        IconButton(onClick = onNext) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = DarkBlue)
        }
    }
}

// Неделя
@Composable
private fun WeekStrip(
    weekStart: LocalDate,
    selectedDate: LocalDate,
    meetingsCountByDate: Map<LocalDate, Int>,
    onSelectDate: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        (0..6).forEach { i ->
            val date = weekStart.plusDays(i.toLong())
            val count = meetingsCountByDate[date] ?: 0
            val isSelected = date == selectedDate
            val isToday = date == LocalDate.now()

            WeekDayItem(
                date = date,
                count = count,
                isSelected = isSelected,
                isToday = isToday,
                onClick = { onSelectDate(date) }
            )
        }
    }
}

@Composable
private fun WeekDayItem(
    date: LocalDate,
    count: Int,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru"))
        .replace(".", "")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString() }

    Column(
        modifier = Modifier
            .width(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(if (isSelected) DarkBlue.copy(alpha = 0.08f) else Color.Transparent)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayLabel,
            fontSize = 12.sp,
            color = if (isSelected) DarkBlue else TextGrey,
            fontFamily = FontFamily(Font(R.font.open_sans_semibold))
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 16.sp,
            color = DarkBlue,
            fontFamily = FontFamily(Font(R.font.open_sans_extrabold))
        )

        Spacer(Modifier.height(6.dp))

        val bubbleColor = if (count > 0) DarkBlue else TextGrey.copy(alpha = 0.35f)

        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(bubbleColor),
            contentAlignment = Alignment.Center
        ) {
            if (count > 0) {
                Text(
                    text = if (count > 9) "9+" else count.toString(),
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (isToday) {
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
        }
    }
}

// Месяц
@Composable
private fun MonthCalendar(
    anchorDate: LocalDate,
    selectedDate: LocalDate,
    meetingsCountByDate: Map<LocalDate, Int>,
    onSelectDate: (LocalDate) -> Unit,
    onDoubleClickDate: (LocalDate) -> Unit,
) {
    val ym = remember(anchorDate) { YearMonth.from(anchorDate) }
    val firstOfMonth = remember(ym) { ym.atDay(1) }
    val daysInMonth = remember(ym) { ym.lengthOfMonth() }

    val leadingEmpty = remember(firstOfMonth) { firstOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .border(1.dp, TextGrey.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        MonthWeekHeader()

        Spacer(Modifier.height(8.dp))

        val totalCells = leadingEmpty + daysInMonth
        val rows = ((totalCells + 6) / 7)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (col in 0..6) {
                        val cellIndex = row * 7 + col
                        val dayNumber = cellIndex - leadingEmpty + 1

                        if (dayNumber < 1 || dayNumber > daysInMonth) {
                            MonthEmptyCell()
                        } else {
                            val date = ym.atDay(dayNumber)
                            val count = meetingsCountByDate[date] ?: 0

                            MonthDayCell(
                                date = date,
                                count = count,
                                isSelected = date == selectedDate,
                                isToday = date == LocalDate.now(),
                                onClick = { onSelectDate(date) },
                                onDoubleClick = { onDoubleClickDate(date) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthWeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val days = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
        days.forEach { d ->
            Text(
                text = d,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = TextGrey,
                fontFamily = FontFamily(Font(R.font.open_sans_semibold))
            )
        }
    }
}

@Composable
private fun MonthEmptyCell() {
    Box(modifier = Modifier.size(40.dp))
}

@Composable
private fun MonthDayCell(
    date: LocalDate,
    count: Int,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit
) {
    val bg = when {
        isSelected -> DarkBlue.copy(alpha = 0.12f)
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // число
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.open_sans_extrabold)),
            color = DarkBlue
        )

        // точки/полоска встреч снизу
        if (count > 0) {
            val maxDots = 5

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
                    .height(4.dp)
                    .fillMaxWidth(0.9f),
                contentAlignment = Alignment.Center
            ) {
                if (count <= maxDots) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(count) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(DarkBlue)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(DarkBlue)
                    )
                }
            }
        }

        // маркер "сегодня"
        if (isToday) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
        }
    }
}



// доп функции
private fun startOfWeek(date: LocalDate): LocalDate {
    val dow = date.dayOfWeek.value // Mon=1..Sun=7
    return date.minusDays((dow - DayOfWeek.MONDAY.value).toLong())
}

private fun clampSelectedDate(viewType: HomeViewType, anchorDate: LocalDate, selected: LocalDate): LocalDate {
    return when (viewType) {
        HomeViewType.WEEK -> {
            val start = startOfWeek(anchorDate)
            val end = start.plusDays(6)
            if (selected.isBefore(start) || selected.isAfter(end)) anchorDate else selected
        }
        HomeViewType.MONTH -> anchorDate
    }
}

private fun formatShortDate(d: LocalDate): String {
    val month = d.month.getDisplayName(TextStyle.SHORT, Locale("ru")).replace(".", "")
    return "${d.dayOfMonth} $month"
}

private fun monthNameNominative(monthValue: Int): String {
    val months = listOf(
        "январь", "февраль", "март", "апрель", "май", "июнь",
        "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"
    )
    return months[monthValue - 1]
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString() }
}
