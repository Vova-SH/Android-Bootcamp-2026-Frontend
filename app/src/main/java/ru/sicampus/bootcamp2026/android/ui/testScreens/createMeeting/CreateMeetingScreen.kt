package ru.sicampus.bootcamp2026.android.ui.testScreens.createMeeting

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.ui.components.CustomNavigationBar
import ru.sicampus.bootcamp2026.android.ui.components.CustomTextField
import ru.sicampus.bootcamp2026.android.ui.theme.Black
import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.android.ui.theme.ErrorRed
import ru.sicampus.bootcamp2026.android.ui.theme.Grey
import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
import ru.sicampus.bootcamp2026.android.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeetingScreen(
    navController: NavHostController,
    viewModel: CreateMeetingViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is CreateMeetingAction.NavigateTo -> navController.navigate(action.route)
                is CreateMeetingAction.MeetingCreated -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            CustomNavigationBar(
                navController = navController
            )
        }
    ) { paddingValues ->
        when (val currentState = state) {
            is CreateMeetingState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(stringResource(R.string.create_meeting))
                    }
                }
            }

            is CreateMeetingState.Data -> {
                CreateMeetingContent(
                    state = currentState,
                    paddingValues = paddingValues,
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMeetingContent(
    state: CreateMeetingState.Data,
    paddingValues: PaddingValues,
    viewModel: CreateMeetingViewModel
) {
    var expandedStartHour by remember { mutableStateOf(false) }
    var expandedEndHour by remember { mutableStateOf(false) }
    val hours = (0..23).map { it.toString() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                bottom = paddingValues.calculateBottomPadding()
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))

        Text(
            text = stringResource(R.string.create_meeting),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.open_sans_bold))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Название
        CustomTextField(
            value = state.title,
            onValueChange = { viewModel.onIntent(CreateMeetingIntent.UpdateTitle(it)) },
            label = stringResource(R.string.meeting_name),
            modifier = Modifier,
            textColor = TextGrey
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Описание
        CustomTextField(
            value = state.description,
            onValueChange = { viewModel.onIntent(CreateMeetingIntent.UpdateDescription(it)) },
            label = stringResource(R.string.description),
            modifier = Modifier,
            textColor = TextGrey
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Поиск участников
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onIntent(CreateMeetingIntent.UpdateSearchQuery(it)) },
                label = stringResource(R.string.email_employee),
                textColor = TextGrey,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Search,
                keyboardActionOnDone = {
                    viewModel.onIntent(CreateMeetingIntent.SearchPerson)
                }
            )

            // Результаты поиска
            if (state.isSearching) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.padding(start = 25.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.search_employee),
                        fontSize = 12.sp,
                        color = TextGrey
                    )
                }
            } else if (state.searchResults.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                state.searchResults.forEach { person ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 5.dp)
                            .clickable {
                                viewModel.onIntent(
                                    CreateMeetingIntent.AddParticipant(
                                        Participant(
                                            id = person.id,
                                            email = person.email,
                                            fullName = person.fullName
                                        )
                                    )
                                )
                            }
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Unspecified
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = person.fullName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = person.email,
                                fontSize = 12.sp,
                                color = TextGrey
                            )
                        }
                    }
                }
            } else if (state.searchQuery.isNotBlank() && !state.isSearching && state.searchQuery.length >= 3) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.no_results),
                    fontSize = 14.sp,
                    color = TextGrey,
                    modifier = Modifier.padding(start = 25.dp)
                )
            }

            // Список участников
            if (state.participants.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.add_participant),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 25.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                state.participants.forEach { participant ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Unspecified
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = participant.fullName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = participant.email,
                                    fontSize = 12.sp,
                                    color = TextGrey
                                )
                            }
                            IconButton(
                                onClick = {
                                    viewModel.onIntent(
                                        CreateMeetingIntent.RemoveParticipant(participant.id)
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Удалить",
                                    tint = ErrorRed
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Дата
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar_icon),
                contentDescription = "Дата встречи",
                modifier = Modifier.padding(start = 25.dp)
            )

            CustomTextField(
                value = state.date,
                onValueChange = { viewModel.onIntent(CreateMeetingIntent.UpdateDate(it)) },
                label = "дд.мм.гггг",
                modifier = Modifier.weight(1f),
                textColor = TextGrey,
                keyboardType = KeyboardType.Number
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Время начала
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 27.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.clock_icon),
                contentDescription = "Время начала"
            )

            Spacer(modifier = Modifier.width(25.dp))

            Column(

            ){
                ExposedDropdownMenuBox(
                    expanded = expandedStartHour,
                    onExpandedChange = { expandedStartHour = !expandedStartHour },
                ) {
                    TextField(
                        value = if (state.startHour.isBlank()) "Начало встречи" else "${state.startHour}:00",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStartHour) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Black,
                            unfocusedTextColor = Black,
                            focusedContainerColor = Grey,
                            unfocusedContainerColor = Grey,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Grey,
                            disabledTextColor = TextGrey,
                            disabledLabelColor = Black,
                            disabledIndicatorColor = Color.Transparent
                        ),

                        shape = RoundedCornerShape(40.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedStartHour,
                        onDismissRequest = { expandedStartHour = false }
                    ) {
                        hours.forEach { hour ->
                            DropdownMenuItem(
                                text = { Text("$hour:00") },
                                onClick = {
                                    viewModel.onIntent(CreateMeetingIntent.UpdateStartHour(hour))
                                    expandedStartHour = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedEndHour,
                    onExpandedChange = { expandedEndHour = !expandedEndHour },
                ) {
                    TextField(
                        value = if (state.endHour.isBlank()) "Конец встречи" else "${state.endHour}:00",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEndHour) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Black,
                            unfocusedTextColor = Black,
                            focusedContainerColor = Grey,
                            unfocusedContainerColor = Grey,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Grey,
                            disabledTextColor = TextGrey,
                            disabledLabelColor = Black,
                            disabledIndicatorColor = Color.Transparent
                        ),

                        shape = RoundedCornerShape(40.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedEndHour,
                        onDismissRequest = { expandedEndHour = false }
                    ) {
                        hours.forEach { hour ->
                            DropdownMenuItem(
                                text = { Text("$hour:00") },
                                onClick = {
                                    viewModel.onIntent(CreateMeetingIntent.UpdateEndHour(hour))
                                    expandedEndHour = false
                                }
                            )
                        }
                    }
                }

            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        if (state.validationHint != null && !state.isEnabledCreate) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .border(
                        width = 1.dp,
                        color = TextGrey,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 13.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.validationHint,
                    color = TextGrey,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Кнопка создания
        Button(
            onClick = { viewModel.onIntent(CreateMeetingIntent.CreateMeeting) },
            modifier = Modifier
                .padding(horizontal = 64.dp)
                .fillMaxWidth()
                .height(48.dp),
            enabled = state.isEnabledCreate,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkBlue,
                contentColor = White,
                disabledContainerColor = DarkBlue.copy(alpha = 0.5f),
                disabledContentColor = White.copy(alpha = 0.5f)
            )
        ) {
            Text(stringResource(R.string.create_button), fontWeight = FontWeight.Bold)
        }

        // Ошибка
        if (state.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .border(
                        width = 2.dp,
                        color = ErrorRed,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 13.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error,
                    color = ErrorRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}