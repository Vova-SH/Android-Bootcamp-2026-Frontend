package ru.sicampus.bootcamp2026.ui.screen.meetings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.FlowPreview
import ru.sicampus.bootcamp2026.components.MeetingItem
import ru.sicampus.bootcamp2026.components.Title
import ru.sicampus.bootcamp2026.ui.screen.inbox.InboxIntent
import ru.sicampus.bootcamp2026.ui.screen.meetings.calendar.Calendar
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Orange
import ru.sicampus.bootcamp2026.ui.theme.Red
import ru.sicampus.bootcamp2026.ui.theme.Violet
import java.time.LocalDate

@OptIn(FlowPreview::class)
@Composable

fun MeetingsScreen(
    viewmodel: MeetingViewModel = viewModel<MeetingViewModel>(),
) {
    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        Calendar(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Title(title = "Сегодня")

            // InboxItem()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                item {
                    Spacer(modifier = Modifier.height(58.dp))
                }

                repeat(3) {
                    item {
                        MeetingItem(
                            background = Red,
                            title = "Android Bootcamp 2026",
                            place = "Общежитие МИФИ",
                            time = "18:00",
                            status = "Вы участник"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        MeetingItem(
                            background = Violet,
                            title = "НТО РМП ФИНАЛЬНЫЙ ФИНАЛ 2025-2026",
                            place = "Столовая МИФИ",
                            time = "19:00",
                            status = "Вы участник"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        MeetingItem(
                            background = Blue,
                            title = "Android Bootcamp 2026",
                            place = "Общежитие МИФИ",
                            time = "20:00",
                            status = "Вы организатор"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        MeetingItem(
                            background = Orange,
                            title = "Android Bootcamp 2026",
                            place = "Общежитие МИФИ",
                            time = "21:00",
                            status = "Вы участник"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(128.dp))
                }
            }
        }
    }
}