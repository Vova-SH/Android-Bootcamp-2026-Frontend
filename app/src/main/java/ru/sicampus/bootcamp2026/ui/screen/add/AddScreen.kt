package ru.sicampus.bootcamp2026.ui.screen.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.domain.add.entities.TimeSlotEntity
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity
import ru.sicampus.bootcamp2026.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(onReturnBack: () -> Unit, viewModel: AddViewModel = viewModel(), ) {
    val user = remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        user.value = AuthLocalDataSource.getCurrentUser()
    }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is AddState.Error -> AddErrorState(
            currentState,
            onRefresh = {
                viewModel.onIntent(AddIntent.Refresh)
            })
        is AddState.Loading -> AddLoadingState()
        is AddState.Content -> AddContentState(
            currentState,
            user,
            viewModel,
            onReturnBack,
            selectedDate = selectedDate,
            onDateChange = { newDate -> selectedDate = newDate },
            onRefresh = { viewModel.onIntent(AddIntent.Refresh) },
            onLoadMore = { viewModel.onIntent(AddIntent.LoadMore) }
        )
    }

}

@Composable
private  fun AddLoadingState(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private  fun AddErrorState( state: AddState.Error, onRefresh: () -> Unit ){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(
                onClick = onRefresh
            ){
                Text("Refresh")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddContentState(
    state: AddState.Content,
    user: MutableState<UserDto?>,
    viewModel: AddViewModel,
    onReturnBack: () -> Unit,
    selectedDate: LocalDate?,
    onDateChange: (LocalDate?) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,

){
    val organizerId = user.value?.id
    var selectedTimeSlot by remember { mutableStateOf<TimeSlotEntity?>(null) }

    var showParticipantsPicker by remember { mutableStateOf(false) }
    var showTimeSlots by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var meetingDescription by remember { mutableStateOf(TextFieldValue()) }
    var meetingTitle by remember { mutableStateOf(TextFieldValue()) }
    var selectedParticipantIds by remember { mutableStateOf(listOf<Int>()) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    ) // для кастомного data pickera


    val timeSlotsState by viewModel.timeSlotsState.collectAsState()
//    val createState by viewModel.createState.collectAsState()

    LaunchedEffect(selectedDate) {
        selectedDate?.let { date ->
            viewModel.getTime(date.toString())
        }
    }

    var participants by remember { mutableStateOf( state.users ) }
    //var participants by remember(state.users) { mutableStateOf( state.users ) }

    val toggleParticipant: (Int) -> Unit = remember {
        { userId ->
            selectedParticipantIds = if (selectedParticipantIds.contains(userId)) {
                selectedParticipantIds - userId
            } else {
                selectedParticipantIds + userId
            }
        }
    }

    val selectedParticipants = remember(state.users, selectedParticipantIds) {
        state.users.filterIsInstance<AddState.Item.User>().map { it.entity }
            .filter { it.id in selectedParticipantIds }
    }

    val lazyColumnListState = rememberLazyListState()
    val isNeededLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: Int.MIN_VALUE
            val totalItems = lazyColumnListState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 7
        }
    }

    LaunchedEffect(isNeededLoadMore, state.isLastPage) {
        if(isNeededLoadMore && !state.isLastPage){
            onLoadMore.invoke()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 56.dp)
        ) {
            // заголовок
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // стрелка
                IconButton(
                    onClick = { onReturnBack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = "Назад",
                        tint = Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "создать встречу",
                    color = Color.Black,
                    style = CustomTypography.displayMedium
                )

                IconButton(
                    onClick = {
                        viewModel.createEvent(
                            organizerId!!,
                            meetingTitle,
                            meetingDescription,
                            selectedDate.toString(),
                            selectedTimeSlot?.startTime ?: "00:00:00",
                            selectedTimeSlot?.endTime ?: "00:00:00",
                            selectedParticipantIds
                        )
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Сохранить",
                        tint = Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // название встречи
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(30.dp))
                        .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                        .padding(16.dp)
                ) {
                    BasicTextField(
                        value = meetingTitle,   //TODO
                        onValueChange = { meetingTitle = it },
                        textStyle = CustomTypography.labelMedium.copy(
                            color = if (meetingTitle.text.isEmpty()) VeryDarkGrey else Color.Black
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (meetingTitle.text.isEmpty()) {
                                    Text(
                                        text = "название встречи*",
                                        style = CustomTypography.labelMedium,
                                        color = VeryDarkGrey
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Текст ошибки под названием
                if (meetingTitle.text.isEmpty()) {
                    Text(
                        text = "* Заполните название задачи",
                        style = CustomTypography.labelSmall,
                        color = Red,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Описание
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = meetingDescription,  //TODO
                    onValueChange = { meetingDescription = it },
                    textStyle = CustomTypography.labelMedium.copy(
                        color = if (meetingDescription.text.isEmpty()) MediumGray else Color.Black
                    ),
                    maxLines = 5,
                    decorationBox = { innerTextField ->
                        Box {
                            if (meetingDescription.text.isEmpty()) {
                                Text(text = "описание", style = CustomTypography.labelMedium, color = VeryDarkGrey)
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Выбрать дату
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .border(
                        width = 1.dp,
                        color = MediumGray,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .clickable { showDatePicker = true }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                            ?: "установить дату",
                        style = CustomTypography.labelMedium,
                        color = if (selectedDate == null) VeryDarkGrey else Color.Black
                    )

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Выбрать дату",
                        tint = VeryDarkGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Выбрать доступный слот времени
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                    .clickable { showTimeSlots = true }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedTimeSlot?.startTime ?: "выбрать доступный слот",
                        style = CustomTypography.labelMedium,
                        color = if (selectedTimeSlot == null) VeryDarkGrey else Color.Black
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Раскрыть список",
                        tint = VeryDarkGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Добавить участников
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                    .clickable { showParticipantsPicker = true }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "добавить участников",
                        style = CustomTypography.labelMedium,
                        color = VeryDarkGrey
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Раскрыть список",
                        tint = VeryDarkGrey
                    )
                }
            }

            // выбранные участники
            if (selectedParticipants.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "участники",
                        style = CustomTypography.labelMedium,
                        color = Black.copy(alpha = 0.5f),
                        modifier = Modifier.padding(start = 10.dp, bottom = 12.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items = selectedParticipants, key = { it.id }) { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(30.dp))
                                    .border(width = 1.dp, color = DarkGray1, shape = RoundedCornerShape(30.dp))
                                    .padding(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // аватарка
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(30.dp))
                                            .background(MediumGray)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                            contentDescription = "Аватар пользователя",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = item.fullName,
                                        style = CustomTypography.labelMedium,
                                        color = Black
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    IconButton(
                                        onClick = {
                                            selectedParticipantIds = selectedParticipantIds - item.id
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.cross),
                                            contentDescription = "Удалить",
                                            tint = Red,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // полупрозрачный фон для закрытия dropdown кликом вокруг
        if (showTimeSlots || showParticipantsPicker) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        showTimeSlots = false
                        showParticipantsPicker = false
                    }
            )
        }

        if (showTimeSlots) {
            when (val timeState = timeSlotsState) {
                is SlotsState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.TopCenter)
                            .padding(top = 300.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                        Text("выберите дату")
                    }
                }

                is SlotsState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.TopCenter)
                            .padding(top = 300.dp)
                    ) {
                        Column {
                            Text(timeState.reason)
                            Button(onClick = {
                                selectedDate?.let { viewModel.getTime(it.toString()) }
                            }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                is SlotsState.Content -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.TopCenter)
                            .padding(top = 300.dp, start = 16.dp, end = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(30.dp))
                                .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                                .background(Color.White)
                        ) {
                            LazyColumn(modifier = Modifier.height(300.dp)) {
                                items(timeState.timeSlots) { slot ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
                                            .clickable {
                                                selectedTimeSlot = slot
                                                showTimeSlots = false
                                            }
                                    ) {
                                        Text(text = slot.startTime, style = CustomTypography.labelMedium, color = Black)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // dropdown для выбора участников
        if (showParticipantsPicker) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
                    .padding(top = 400.dp, start = 16.dp, end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                        .background(Color.White)
                ) {
                    LazyColumn( state = lazyColumnListState) {
                        items(participants) { item ->
                            when(item){
                                is AddState.Item.Error -> ItemError( onRefresh )
                                is AddState.Item.Loading -> ItemLoading()
                                is AddState.Item.User -> ItemUser(
                                    user = item.entity,
                                    isSelected =  selectedParticipantIds.contains(item.entity.id),
                                    onToggle = { toggleParticipant(item.entity.id) })
                            }
                        }
                    }
                }
            }
        }

        val datePickerColors = DatePickerDefaults.colors(
            containerColor = SoftWhite, // фон диалога
            titleContentColor = Black, // цвет заголовка
            headlineContentColor = SineyIney, // Цвет выбранной даты
            weekdayContentColor = Black, // цвет дней недели
            subheadContentColor = Black, // цвет месяца/года
            navigationContentColor = SineyIney, // цвет кнопок навигации
            yearContentColor = Black, // цвет года в селекторе
            currentYearContentColor = SineyIney, // цвет текущего года
            selectedYearContentColor = SineyIney, // цвет выбранного года
            dayContentColor = Black, // Цвет дней
            selectedDayContentColor = SoftWhite, // Цвет текста выбранного дня
            selectedDayContainerColor = SineyIney, // Фон выбранного дня
            todayContentColor = SineyIney, // Цвет сегодняшней даты
            todayDateBorderColor = SineyIney // граница сегодняшней даты
        )

        // DatePicker Dialog с кастомными цветами
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                colors = datePickerColors,
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val newDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                onDateChange(newDate)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK", color = SineyIney)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Отмена", color = Black)
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = datePickerColors
                )
            }
        }
    }
}
@Composable
private fun ItemError( onRefresh: () -> Unit ){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onRefresh
            ){
                Text("Refresh")
            }
        }
    }
}

@Composable
private fun ItemLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}
@Composable
private fun ItemUser(
    user: UserEntity,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Чекбокс
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(
                    width = 1.dp,
                    color = if (isSelected) Black else MediumGray,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(
                    if (isSelected) Black else Color.White
                )
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = "Выбрано",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = user.fullName,
            style = CustomTypography.labelMedium,
            color = Black
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CreateMeetingScreenPreview() {
//    AddScreen()
//}