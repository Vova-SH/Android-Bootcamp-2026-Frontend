package ru.sicampus.bootcamp2026.ui.screens.book

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.root.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.root.theme.BlueMain
import ru.sicampus.bootcamp2026.ui.root.theme.GrayTextColor
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BookScreen(
    navHostController: NavHostController,
    context: Context,
    vm: BookViewModel = viewModel(factory = BookViewModelFactory.create(context))
) {
    val state by vm.state.collectAsState()

    val showDatePicker = remember { mutableStateOf(false) }
    val showStartTimePicker = remember { mutableStateOf(false) }
    val showEndTimePicker = remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navHostController.popBackStack()
            vm.clearSuccess()
        }
    }
    Box(contentAlignment = Alignment.Center) {
        LazyColumn(
            Modifier.fillMaxSize().background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    Modifier.height(50.dp).fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                        .background(Color.White)
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        TextButton(
                            onClick = {
                                navHostController.popBackStack()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = GrayTextColor,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            stringResource(R.string.new_meet),
                            Modifier.align(Alignment.CenterVertically).weight(2f),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                        TextButton(
                            onClick = {
                                vm.createMeeting()
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !state.isLoading && validateMeeting(state)
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = BlueMain
                                )
                            } else {
                                Text(
                                    stringResource(R.string.success),
                                    color = if (validateMeeting(state)) BlueMain else GrayTextColor,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
                state.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Text(
                    stringResource(R.string.main_word),
                    Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, bottom = 10.dp),
                    color = GrayTextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                CustomTextField2(
                    value = state.title,
                    onValueChange = vm::onTitleChange,
                    placeholder = stringResource(R.string.label),
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .shadow(3.dp, RoundedCornerShape(15.dp)),
                    height = 50
                )
                CustomTextField2(
                    value = state.description,
                    onValueChange = vm::onDescriptionChange,
                    placeholder = stringResource(R.string.description),
                    modifier = Modifier.height(110.dp)
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .shadow(3.dp, RoundedCornerShape(15.dp)),
                    height = 110
                )

                Text(
                    stringResource(R.string.date_and_place),
                    modifier = Modifier.fillMaxWidth().padding(top = 25.dp, start = 16.dp),
                    color = GrayTextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                DatePickerForBook(
                    currentDate = state.selectedDate,
                    currentTime = state.selectedStartTime,
                    cabinet = state.cabinet,
                    onCabinetChange = vm::onCabinetChange,
                    onDateClick = { showDatePicker.value = true },
                    onStartTimeClick = { showStartTimePicker.value = true },
                    onEndTimeClick = { showEndTimePicker.value = true },
                    endTime = state.selectedEndTime
                )
                Text(
                    stringResource(R.string.participant),
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 16.dp),
                    color = GrayTextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Box(
                    Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 7.dp)
                        .height(70.dp)
                        .shadow(3.dp, RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.White)
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Row(
                            Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${state.selectedUsers.size} из 20",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.width(10.dp))
                            Box(Modifier.weight(1f)) {
                                CustomTextField2(
                                    value = state.searchQuery,
                                    onValueChange = vm::onSearchQueryChange,
                                    placeholder = stringResource(R.string.search),
                                    height = 40,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(5.dp))
                if (state.selectedUsers.isNotEmpty()) {
                    val selectedUserDetails =
                        state.searchResults.filter { it.id in state.selectedUsers }
                    if (selectedUserDetails.isNotEmpty()) {
                        Text(
                            "Выбранные участники:",
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 16.dp, bottom = 8.dp),
                            color = GrayTextColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedUserDetails.forEach { user ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = BlueMain.copy(alpha = 0.1f)
                                    ),
                                    modifier = Modifier.padding(2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    ) {
                                        AsyncImage(
                                            model = user.photoUrl,
                                            contentDescription = "",
                                            modifier = Modifier.size(24.dp).clip(CircleShape),
                                            error = painterResource(R.drawable.ic_launcher_foreground)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            "${user.firstName} ${user.secondName}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable { vm.removeParticipant(user.id) }
                                        ) {
                                            Text(
                                                "×",
                                                fontSize = 14.sp,
                                                color = Color.Red,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (state.isLoading && state.searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BlueMain)
                    }
                } else if (state.searchResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 400.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                        state = lazyListState
                    ) {
                        items(state.searchResults) { user ->
                            val isSelected = user.id in state.selectedUsers

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) BlueMain.copy(alpha = 0.1f) else Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) BlueMain else Color.LightGray.copy(
                                            alpha = 0.3f
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                onClick = {
                                    if (isSelected) {
                                        vm.removeParticipant(user.id)
                                    } else {
                                        if (state.selectedUsers.size < 20) {
                                            vm.addParticipant(user)
                                        }
                                    }
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = user.photoUrl,
                                        contentDescription = "${user.firstName} ${user.secondName}",
                                        modifier = Modifier.size(40.dp).clip(CircleShape),
                                        error = painterResource(R.drawable.ic_launcher_foreground)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "${user.firstName} ${user.secondName}",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp
                                        )
                                    }
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .background(BlueMain, CircleShape)
                                        ) {
                                            Text(
                                                "✓",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .border(
                                                    width = 2.dp,
                                                    color = Color.LightGray,
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            if (state.isLoading && state.searchResults.isNotEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = BlueMain)
                                }
                            } else if (!state.isLastPage && state.searchQuery.isNotEmpty()) {
                                LaunchedEffect(lazyListState) {
                                    if (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ==
                                        lazyListState.layoutInfo.totalItemsCount - 1
                                    ) {
                                        vm.searchUsers()
                                    }
                                }
                            }
                        }
                    }
                } else if (state.searchQuery.isNotEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пользователи не найдены",
                            color = GrayTextColor,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        if (showDatePicker.value) {
            DatePickerModal(
                initialDate = state.selectedDate,
                onDateSelected = { selectedDate ->
                    selectedDate?.let {
                        vm.onDateChange(Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate())
                    }
                    showDatePicker.value = false
                },
                onDismiss = { showDatePicker.value = false }
            )
        }

        if (showStartTimePicker.value) {
            TimePickerModal(
                initialTime = state.selectedStartTime,
                onTimeSelected = { hour, minute ->
                    vm.onStartTimeChange(LocalTime.of(hour, minute))
                    val newStartTime = LocalTime.of(hour, minute)
                    if (newStartTime >= state.selectedEndTime) {
                        vm.onEndTimeChange(newStartTime.plusHours(1))
                    }
                    showStartTimePicker.value = false
                },
                onDismiss = { showStartTimePicker.value = false }
            )
        }

        if (showEndTimePicker.value) {
            TimePickerModal(
                initialTime = state.selectedEndTime,
                onTimeSelected = { hour, minute ->
                    vm.onEndTimeChange(LocalTime.of(hour, minute))
                    showEndTimePicker.value = false
                },
                onDismiss = { showEndTimePicker.value = false }
            )
        }
    }
}


private fun validateMeeting(state: BookUiState): Boolean {
    return state.title.isNotBlank() &&
            state.description.isNotBlank() &&
            state.selectedStartTime < state.selectedEndTime &&
            state.cabinet.isNotBlank() &&
            state.cabinet != "Не выбрано"
}

@Composable
fun DatePickerForBook(
    currentDate: LocalDate,
    currentTime: LocalTime,
    cabinet: String,
    onCabinetChange: (String) -> Unit,
    endTime: LocalTime,
    onDateClick: () -> Unit,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit
) {
    Box(
        Modifier
            .padding(start = 16.dp, end = 16.dp, top = 10.dp)
            .shadow(3.dp, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth().padding(start = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        stringResource(R.string.date),
                        fontSize = 12.sp,
                        color = GrayTextColor,
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${currentDate.dayOfMonth}.${currentDate.monthValue}.${currentDate.year}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 5.dp, bottom = 2.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(
                    onClick = onDateClick,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Outlined.DateRange, "")
                }
            }
            HorizontalDivider(color = Color(0xffD9D9D9), thickness = 1.dp)
            Row(Modifier.height(48.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(Modifier.padding(top = 5.dp, start = 14.dp)) {
                        Text(
                            stringResource(R.string.start),
                            fontSize = 12.sp,
                            color = GrayTextColor,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            currentTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                        )
                    }
                    IconButton(onClick = onStartTimeClick) {
                        Icon(
                            painterResource(R.drawable.clock),
                            "",
                            Modifier.size(16.dp)
                        )
                    }
                }
                VerticalDivider()
                Row(Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(Modifier.padding(top = 5.dp, start = 14.dp)) {
                            Text(
                                stringResource(R.string.end),
                                fontSize = 12.sp,
                                color = GrayTextColor,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                endTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                            )
                        }
                        IconButton(onClick = onEndTimeClick) {
                            Icon(
                                painterResource(R.drawable.clock),
                                "",
                                Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
            HorizontalDivider()
            Row {
                Column(Modifier.padding(start = 5.dp, top = 4.dp)) {
                    Text(
                        stringResource(R.string.cabinet),
                        fontSize = 12.sp,
                        color = GrayTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    CustomTextField3(
                        value = cabinet,
                        onValueChange = onCabinetChange,
                        placeholder = "Не выбрано",
                        height = 30,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                }
            }
            Spacer(Modifier.size(5.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    initialTime: LocalTime,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true,
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .shadow(3.dp, RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.White,
                    selectorColor = BlueMain.copy(0.8f),
                    timeSelectorSelectedContainerColor = Color(0xff155DFC).copy(0.4f),
                    timeSelectorUnselectedContainerColor = Color.White
                )
            )
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel), color = GrayTextColor)
                }
                TextButton(onClick = {
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                }) {
                    Text(stringResource(R.string.confirm), color = BlueMain)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate: LocalDate,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                dayInSelectionRangeContentColor = Color.White
            ),
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }) {
                    Text(stringResource(R.string.ok), color = BlueMain)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel), color = Color(0xff6E6C6C))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    selectedDayContainerColor = BlueMain.copy(0.8f),
                    todayDateBorderColor = BlueMain.copy(0.8f),
                    todayContentColor = BlueMain.copy(0.8f)
                )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    height: Int
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val elevation by animateDpAsState(
        targetValue = if (isFocused) 8.dp else 0.dp,
        animationSpec = tween(durationMillis = 200)
    )

    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(15.dp)
            )
            .border(
                width = 1.dp,
                color = if (isFocused) BlueMain.copy(alpha = 0.6f)
                else Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .background(
                color = if (enabled) Color.White else Color.LightGray
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusRequester.requestFocus() },
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Spacer(modifier = Modifier.width(1.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .height(height.dp)
                    .fillMaxWidth(0.9f),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = TextStyle(
                    color = if (enabled) Color.Black else Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (height != 110) Alignment.CenterStart else Alignment.TopStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = GrayTextColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextField3(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    height: Int
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val elevation by animateDpAsState(
        targetValue = if (isFocused) 8.dp else 0.dp,
        animationSpec = tween(durationMillis = 200)
    )

    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(bottomStart =  15.dp, bottomEnd = 15.dp)
            )
            .border(
                width = 1.dp,
                color = if (isFocused) BlueMain.copy(alpha = 0.6f)
                else Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(bottomStart =  15.dp, bottomEnd = 15.dp)
            )
            .clip(RoundedCornerShape(bottomStart =  15.dp, bottomEnd = 15.dp))
            .background(
                color = if (enabled) Color.White else Color.LightGray
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusRequester.requestFocus() },
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Spacer(modifier = Modifier.width(1.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .height(height.dp)
                    .fillMaxWidth(0.9f),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = TextStyle(
                    color = if (enabled) Color.Black else Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (height != 110) Alignment.CenterStart else Alignment.TopStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = GrayTextColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}