package ru.sicampus.bootcamp2026.android.ui.screenSchemes
//
//import ru.sicampus.bootcamp2026.android.ui.components.MeetingCard
//import ru.sicampus.bootcamp2026.android.ui.components.MeetingCardActions
//import ru.sicampus.bootcamp2026.android.ui.components.MeetingUi
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ChevronLeft
//import androidx.compose.material.icons.filled.ChevronRight
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import ru.sicampus.bootcamp2026.R
//import ru.sicampus.bootcamp2026.android.ui.components.CustomNavigationBar
//import ru.sicampus.bootcamp2026.android.ui.theme.AppTheme
//import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
//import ru.sicampus.bootcamp2026.android.ui.theme.IconsGrey
//import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
//import java.time.DayOfWeek
//import java.time.LocalDate
//import java.time.YearMonth
//import java.time.format.TextStyle
//import java.util.Locale
//
//
//private enum class HomeViewType(val title: String) {
//    WEEK("Неделя"),
//    MONTH("Месяц")
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    onNotificationsClick: () -> Unit = {},
//    onCreateMeetingClick: () -> Unit = {},
//    onProfileClick: () -> Unit = {}
//) {
//    var viewType by remember { mutableStateOf(HomeViewType.WEEK) }
//    var showViewTypeMenu by remember { mutableStateOf(false) }
//
//    // неотвеченные приглашения, уведы
//    val pendingInvitesCount by remember { mutableStateOf(2) }
//
//    var anchorDate by remember { mutableStateOf(LocalDate.now()) }
//
//    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
//
//    // кружочек с кол-во встреч
//    val meetingsCountByDate = remember {
//        mapOf(
//            LocalDate.now() to 2,
//            LocalDate.now().plusDays(1) to 1,
//            LocalDate.now().plusDays(2) to 3,
//            LocalDate.now().minusDays(2) to 1
//        )
//    }
//
//    Scaffold(
//        bottomBar = {
//            CustomNavigationBar(
//                selectedItem = 0,
//                onHomeClick = {},
//                onCreateMeetingClick = onCreateMeetingClick,
//                onProfileClick = onProfileClick
//            )
//        }
//    ) { paddingValues ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(Color.White)
//        ) {
//
//            // верхняя строка
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box {
//                    Box(
//                        modifier = Modifier
//                            .border(1.dp, TextGrey, RoundedCornerShape(8.dp))
//                            .clickable { showViewTypeMenu = true }
//                            .padding(horizontal = 12.dp, vertical = 8.dp)
//                    ) {
//                        Text(
//                            text = viewType.title,
//                            fontSize = 16.sp,
//                            fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//                            color = DarkBlue
//                        )
//                    }
//
//                    DropdownMenu(
//                        expanded = showViewTypeMenu,
//                        onDismissRequest = { showViewTypeMenu = false }
//                    ) {
//                        HomeViewType.entries.forEach { option ->
//                            DropdownMenuItem(
//                                text = { Text(option.title) },
//                                onClick = {
//                                    viewType = option
//                                    showViewTypeMenu = false
//                                    selectedDate = anchorDate
//                                }
//                            )
//                        }
//                    }
//                }
//
//                IconButton(onClick = onNotificationsClick) {
//                    if (pendingInvitesCount > 0) {
//                        BadgedBox(badge = {
//                            Badge(containerColor = Color.Red, contentColor = Color.White) {}
//                        }) {
//                            Icon(
//                                imageVector = Icons.Default.Notifications,
//                                contentDescription = "Notifications",
//                                tint = DarkBlue,
//                                modifier = Modifier.size(30.dp)
//                            )
//                        }
//                    } else {
//                        Icon(
//                            imageVector = Icons.Default.Notifications,
//                            contentDescription = "Notifications",
//                            tint = IconsGrey,
//                            modifier = Modifier.size(30.dp)
//                        )
//                    }
//                }
//            }
//
//            PeriodNavRow(
//                viewType = viewType,
//                anchorDate = anchorDate,
//                onPrev = {
//                    anchorDate = when (viewType) {
//                        HomeViewType.WEEK -> anchorDate.minusWeeks(1)
//                        HomeViewType.MONTH -> anchorDate.minusMonths(1)
//                    }
//                    selectedDate = clampSelectedDate(viewType, anchorDate, selectedDate)
//                },
//                onNext = {
//                    anchorDate = when (viewType) {
//                        HomeViewType.WEEK -> anchorDate.plusWeeks(1)
//                        HomeViewType.MONTH -> anchorDate.plusMonths(1)
//                    }
//                    selectedDate = clampSelectedDate(viewType, anchorDate, selectedDate)
//                }
//            )
//
//            Spacer(Modifier.height(12.dp))
//
//            when (viewType) {
//                HomeViewType.WEEK -> {
//                    val weekStart = startOfWeek(anchorDate)
//
//                    WeekStrip(
//                        weekStart = weekStart,
//                        selectedDate = selectedDate,
//                        meetingsCountByDate = meetingsCountByDate,
//                        onSelectDate = { selectedDate = it }
//                    )
//
//                    Spacer(Modifier.height(12.dp))
//
//                    MeetingsForDay(
//                        date = selectedDate,
//                        count = meetingsCountByDate[selectedDate] ?: 0
//                    )
//                }
//
//                HomeViewType.MONTH -> {
//                    MonthHeaderPlaceholder(anchorDate)
//                }
//            }
//        }
//    }
//}
//
//// TODO: здесь будет lazy column потом
//@Composable
//private fun MeetingsForDay(
//    date: LocalDate,
//    count: Int
//) {
//    val meetings = remember(date, count) {
//        List(count.coerceAtMost(3)) { idx ->
//            MeetingUi(
//                organizerName = "Организатор",
//                title = "Встреча ${idx + 1}",
//                description = "Описание встречи.",
//                dateText = "%02d.%02d.%d".format(date.dayOfMonth, date.monthValue, date.year),
//                timeText = "${9 + idx}:00 - ${10 + idx}:00"
//            )
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        if (count == 0) {
//            Text(
//                text = "Нет встреч",
//                fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//                color = TextGrey,
//                fontSize = 14.sp
//            )
//        } else {
//            meetings.forEach { meeting ->
//                MeetingCard(
//                    meeting = meeting,
//                    actions = MeetingCardActions.None
//                )
//            }
//        }
//    }
//}
//
//// навигация влево вправо по неделям/месяцам
//@Composable
//private fun PeriodNavRow(
//    viewType: HomeViewType,
//    anchorDate: LocalDate,
//    onPrev: () -> Unit,
//    onNext: () -> Unit
//) {
//    val title = remember(viewType, anchorDate) {
//        when (viewType) {
//            HomeViewType.WEEK -> {
//                val start = startOfWeek(anchorDate)
//                val end = start.plusDays(6)
//                "${formatShortDate(start)} – ${formatShortDate(end)}"
//            }
//            HomeViewType.MONTH -> {
//                val ym = YearMonth.from(anchorDate)
//                "${ym.month.getDisplayName(TextStyle.FULL, Locale("ru"))} ${ym.year}"
//                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString() }
//            }
//        }
//    }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        IconButton(onClick = onPrev) {
//            Icon(Icons.Default.ChevronLeft, contentDescription = "Prev", tint = DarkBlue)
//        }
//
//        Text(
//            text = title,
//            modifier = Modifier.weight(1f),
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//            fontSize = 16.sp,
//            color = DarkBlue
//        )
//
//        IconButton(onClick = onNext) {
//            Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = DarkBlue)
//        }
//    }
//}
//
//// полоска недели
//@Composable
//private fun WeekStrip(
//    weekStart: LocalDate,
//    selectedDate: LocalDate,
//    meetingsCountByDate: Map<LocalDate, Int>,
//    onSelectDate: (LocalDate) -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        (0..6).forEach { i ->
//            val date = weekStart.plusDays(i.toLong())
//            val count = meetingsCountByDate[date] ?: 0
//            val isSelected = date == selectedDate
//            val isToday = date == LocalDate.now()
//
//            WeekDayItem(
//                date = date,
//                count = count,
//                isSelected = isSelected,
//                isToday = isToday,
//                onClick = { onSelectDate(date) }
//            )
//        }
//    }
//}
//
//@Composable
//private fun WeekDayItem(
//    date: LocalDate,
//    count: Int,
//    isSelected: Boolean,
//    isToday: Boolean,
//    onClick: () -> Unit
//) {
//    val dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru"))
//        .replace(".", "")
//        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString() }
//
//    Column(
//        modifier = Modifier
//            .width(48.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .clickable { onClick() }
//            .background(if (isSelected) DarkBlue.copy(alpha = 0.08f) else Color.Transparent)
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = dayLabel,
//            fontSize = 12.sp,
//            color = if (isSelected) DarkBlue else TextGrey,
//            fontFamily = FontFamily(Font(R.font.open_sans_semibold))
//        )
//
//        Spacer(Modifier.height(6.dp))
//
//        Text(
//            text = date.dayOfMonth.toString(),
//            fontSize = 16.sp,
//            color = DarkBlue,
//            fontFamily = FontFamily(Font(R.font.open_sans_extrabold))
//        )
//
//        Spacer(Modifier.height(6.dp))
//
//        val bubbleColor = if (count > 0) DarkBlue else TextGrey.copy(alpha = 0.35f)
//
//        Box(
//            modifier = Modifier
//                .size(18.dp)
//                .clip(CircleShape)
//                .background(bubbleColor),
//            contentAlignment = Alignment.Center
//        ) {
//            if (count > 0) {
//                Text(
//                    text = if (count > 9) "9+" else count.toString(),
//                    fontSize = 10.sp,
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//
//        if (isToday) {
//            Spacer(Modifier.height(6.dp))
//            Box(
//                modifier = Modifier
//                    .size(4.dp)
//                    .clip(CircleShape)
//                    .background(Color.Red)
//            )
//        }
//    }
//}
//
////TODO: месяц заглушка
//@Composable
//private fun MonthHeaderPlaceholder(anchorDate: LocalDate) {
//    val ym = YearMonth.from(anchorDate)
//    Text(
//        text = "Месяц: ${ym.month.getDisplayName(TextStyle.FULL, Locale("ru"))} ${ym.year}",
//        modifier = Modifier.padding(horizontal = 16.dp),
//        fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//        fontSize = 16.sp,
//        color = DarkBlue
//    )
//}
//
//
//private fun startOfWeek(date: LocalDate): LocalDate {
//    val dow = date.dayOfWeek.value // Mon=1..Sun=7
//    return date.minusDays((dow - DayOfWeek.MONDAY.value).toLong())
//}
//
//private fun clampSelectedDate(viewType: HomeViewType, anchorDate: LocalDate, selected: LocalDate): LocalDate {
//    return when (viewType) {
//        HomeViewType.WEEK -> {
//            val start = startOfWeek(anchorDate)
//            val end = start.plusDays(6)
//            if (selected.isBefore(start) || selected.isAfter(end)) anchorDate else selected
//        }
//        HomeViewType.MONTH -> anchorDate
//    }
//}
//
//private fun formatShortDate(d: LocalDate): String {
//    val month = d.month.getDisplayName(TextStyle.SHORT, Locale("ru")).replace(".", "")
//    return "${d.dayOfMonth} $month"
//}
//
//private fun formatDayTitle(d: LocalDate): String {
//    val dow = d.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")).replace(".", "")
//        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString() }
//    return "$dow, ${formatShortDate(d)}"
//}
//
//@Preview
//@Composable
//fun ShowHomeScreen() {
//    AppTheme {
//        HomeScreen()
//    }
//}