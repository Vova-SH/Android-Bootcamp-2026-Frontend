package ru.sicampus.bootcamp2026.presentation.screen.creating_meeting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.presentation.components.AppButton
import ru.sicampus.bootcamp2026.presentation.components.AppTextField
import ru.sicampus.bootcamp2026.presentation.components.AppTitle
import ru.sicampus.bootcamp2026.presentation.components.ButtonContent
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatingMeetingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFinished: () -> Unit
    // TODO(viewModel)
) {
    val currentDate = java.time.LocalDate.now()
    val currentYear = currentDate.year

    // Генерация данных
    val days = (1..31).map { it.toString() }
    val months = listOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )
    val years = (currentYear..currentYear + 10).map { it.toString() }

    val hours = (0..23).map { String.format("%02d:00", it) }

    var selectedDay by remember { mutableStateOf(days[0]) }
    var selectedMonth by remember { mutableStateOf(months[currentDate.monthValue - 1]) }
    var selectedYear by remember { mutableStateOf(years[0]) }

    var startTime by remember { mutableStateOf(hours[0]) }
    var endTime by remember { mutableStateOf(hours[1].takeIf { it != hours[0] } ?: hours.last()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                title = {},
                navigationIcon = {
                    AppButton(
                        modifier = Modifier
                            .height(56.dp)
                            .width(56.dp),
                        contentPadding = PaddingValues(6.dp),
                        content = ButtonContent.Icon(
                            icon = painterResource(R.drawable.ic_arrow_back),
                            size = 41.dp
                        ),
                        cornerRadius = 10.dp,
                        onClick = onBackClick
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 50.dp)
                .padding(top = 50.dp)
        ) {
            AppTitle(
                modifier = Modifier,
                titleText = stringResource(R.string.creating_meeting_title)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Дата:",
                    style = MaterialTheme.typography.bodyLarge
                )
                DropdownMenuSelector(
                    options = days,
                    selected = selectedDay,
                    onSelected = { selectedDay = it },
                    modifier = Modifier.width(40.dp)
                )
                DropdownMenuSelector(
                    options = months,
                    selected = selectedMonth,
                    onSelected = { selectedMonth = it },
                    modifier = Modifier.width(90.dp)
                )
                DropdownMenuSelector(
                    options = years,
                    selected = selectedYear,
                    onSelected = { selectedYear = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Время
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Время:", // "Время:"
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "с", // "с"
                    style = MaterialTheme.typography.bodyMedium
                )
                DropdownMenuSelector(
                    options = hours,
                    selected = startTime,
                    onSelected = { startTime = it },
                    modifier = Modifier.width(90.dp)
                )
                Text(
                    text = "по", // "по"
                    style = MaterialTheme.typography.bodyMedium
                )
                DropdownMenuSelector(
                    options = hours,
                    selected = endTime,
                    onSelected = { endTime = it }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppTextField(
                    labelText = stringResource(R.string.name_meeting_placeholder)
                )
                AppTextField(
                    labelText = stringResource(R.string.description_meeting_placeholder)
                )
                AppButton(
                    content = ButtonContent.Text(stringResource(R.string.create_button)),
                    onClick = onFinished
                )
            }
        }
    }
}

@Composable
private fun DropdownMenuSelector(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
                .height(28.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = selected,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.widthIn(min = 100.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewCreatingMeetingScreen() {
    AndroidBootcamp2026FrontendTheme {
        CreatingMeetingScreen(
            onBackClick = {},
            onFinished = {}
        )
    }
}