package ru.sicampus.bootcamp2026.book


import android.text.Layout
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BookScreen() {
    var currentDate = remember { mutableStateOf(LocalDate.now()) }
    val currentTime = remember { mutableStateOf(LocalTime.now()) }
    val endTime = remember { mutableStateOf(currentTime.value.plusHours(1))}
    val labelText = remember { mutableStateOf("") }
    val descText = remember { mutableStateOf("") }
    val cabinet = remember { mutableStateOf("Не выбрано") }
    val personCounter = remember { mutableStateOf(0) }
    var showDateP = remember { mutableStateOf(false) }
    var showTimeP = remember { mutableStateOf(false) }
    if (currentTime.value.minute != 0) currentTime.value = currentTime.value.plusHours(1).minusMinutes(currentTime.value.minute.toLong())
    Box(contentAlignment = Alignment.Center) {
        Column(
            Modifier.fillMaxSize().background(Color(0xffEEEEEE)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.height(50.dp).fillMaxWidth().shadow(3.dp, RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)).background(Color.White)) {
                Row(Modifier.fillMaxWidth()) {
                    TextButton(onClick = {}, modifier = Modifier.weight(1f)) {
                        Text(
                            text = "отмена", color = Color(0xff6E6C6C),
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        "Новая встреча",
                        Modifier.align(Alignment.CenterVertically).weight(2f),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )

                    TextButton(onClick = {}, modifier = Modifier.weight(1f)) {
                        Text(
                            "Готово", color = Color(0xff155DFC),
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End,
                            fontSize = 16.sp
                        )
                    }
                }
            }


            Text(
                "Основное",
                Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, bottom = 10.dp),
                color = Color(0xff6E6C6C),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            CustomTextField2(
                value = labelText.value, onValueChange = { labelText.value = it },
                placeholder = "Название", modifier = Modifier.padding(horizontal = 16.dp).shadow(3.dp, RoundedCornerShape(15.dp)),
                height = 50
            )
            CustomTextField2(
                value = descText.value,
                onValueChange = { descText.value = it },
                placeholder = "Описание или повестка",
                modifier = Modifier.height(110.dp).padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    .shadow(3.dp, RoundedCornerShape(15.dp)),
                height = 110
            )
            Text(
                "Время и место",
                modifier = Modifier.fillMaxWidth().padding(top = 25.dp, start = 16.dp),
                color = Color(0xff6E6C6C),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            DatePickerForBook(currentDate, currentTime, cabinet =  cabinet, enabled1 =  showDateP, enabled2 =  showTimeP,
                endTime =  endTime)
            Text(
                "Участники",
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 16.dp),
                color = Color(0xff6E6C6C),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Box(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 7.dp)
                    .height(55.dp)
                    .shadow(3.dp, RoundedCornerShape(15.dp)).clip(RoundedCornerShape(15.dp)).background(Color.White)
            ) {
                Row(
                    Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row() {
                        Image(
                            painterResource(R.drawable.plus), "",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(Modifier.size(10.dp))
                        Text(
                            "Добавить участников", fontSize = 16.sp, color = Color.Black.copy(0.6f),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Text(
                        "${personCounter.value} из 100",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
        if (showDateP.value) {
            showTimeP.value = false
            DatePickerModal(
                onDateSelected = { currentDate.value = Instant.ofEpochMilli(it?:1).atZone(ZoneId.systemDefault()).toLocalDate()},
                onDismiss = { showDateP.value = false }
            )
        }
        if (showTimeP.value) {
            showDateP.value = false
            Box(Modifier.shadow(3.dp, RoundedCornerShape(15.dp)).clip(RoundedCornerShape(15.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                TimePicker1(
                    currentTime = currentTime,
                    onDismiss = { showTimeP.value = false },
                    onCon = {
                        showTimeP.value = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker1(
    currentTime: MutableState<LocalTime>,
    onDismiss: () -> Unit,
    onCon: () -> Unit
) {

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.value.hour,
        initialMinute = currentTime.value.minute,
        is24Hour = true,
    )

    Column(Modifier.fillMaxWidth(0.9f)) {
        TimePicker(
            state = timePickerState,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = TimePickerDefaults.colors(clockDialColor = Color.White,
                selectorColor = Color(0xff155DFC).copy(0.8f),
                timeSelectorSelectedContainerColor = Color(0xff155DFC).copy(0.4f),
                timeSelectorUnselectedContainerColor = Color.White)
        )
        Row(Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color(0xff636363))
            }
            TextButton(onClick = {currentTime.value = currentTime.value.withHour(timePickerState.hour)
                .withMinute(timePickerState.minute)
                onCon()
            }) {
                Text("Подтвердить", color = Color(0xff155DFC))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(colors = DatePickerDefaults.colors(containerColor = Color.White,
        dayInSelectionRangeContentColor = Color.White),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK", color = Color(0xff155DFC))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xff6E6C6C))
            }
        }
    ) {
        DatePicker(state = datePickerState, colors = DatePickerDefaults.colors(containerColor = Color.White,
            selectedDayContainerColor = Color(0xff155DFC).copy(0.8f),
            todayDateBorderColor = Color(0xff155DFC).copy(0.8f),
            todayContentColor = Color(0xff155DFC).copy(0.8f)))
    }
}

@Composable
fun DatePickerForBook(currentDate: MutableState<LocalDate>,
                      currentTime: MutableState<LocalTime>,
                      endTime: MutableState<LocalTime>,
                      cabinet: MutableState<String>,
                      enabled1: MutableState<Boolean>,
                      enabled2: MutableState<Boolean>) {
//    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
//    val currentDateAndTime = sdf.format(currentTime.value)
    endTime.value = currentTime.value.plusHours(1)
    Box(Modifier
        .padding(start = 16.dp, end = 16.dp, top = 10.dp)
        .height(148.dp)
        .shadow(3.dp, RoundedCornerShape(15.dp))
        .clip(RoundedCornerShape(15.dp))
        .background(Color.White)) {
        Column() {
            Row(Modifier.fillMaxWidth().padding(start = 15.dp), horizontalArrangement =
                Arrangement.SpaceBetween) {
                Column() {
                    Text("Дата", fontSize = 12.sp,
                        color = Color(0xff636363),
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("${currentDate.value.dayOfMonth}.${currentDate.value.monthValue}.${currentDate.value.year}",
                        fontSize = 14.sp, modifier = Modifier.padding(top = 5.dp, bottom = 2.dp),
                        fontWeight = FontWeight.SemiBold)
                }
                IconButton(onClick = {
                    enabled1.value = !enabled1.value
                },
                    modifier = Modifier.align(Alignment.CenterVertically)){
                    Icon(Icons.Outlined.DateRange, "")
                }
            }
            HorizontalDivider(color = Color(0xffD9D9D9), thickness = 1.dp)
            Row(Modifier.height(48.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.weight(1f)) {
                    Column(Modifier.padding(top = 5.dp, start = 14.dp)) {
                        Text("Начало", fontSize = 12.sp,
                            color = Color(0xff636363),
                            fontWeight = FontWeight.SemiBold)
                        Text(currentTime.value.format(DateTimeFormatter.ofPattern(
                            "HH:mm", Locale("ru")
                        )), fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 4.dp, bottom = 2.dp))
                    }
                    IconButton(onClick = {enabled2.value = !enabled2.value}) {
                        Icon(painterResource(R.drawable.clock), "",
                            Modifier.size(16.dp))
                    }
                }
                VerticalDivider()
                Row(Modifier.weight(1f)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.weight(1f)) {
                        Column(Modifier.padding(top = 5.dp, start = 14.dp)) {
                            Text("Конец", fontSize = 12.sp,
                                color = Color(0xff636363),
                                fontWeight = FontWeight.SemiBold)
                            Text(endTime.value.format(DateTimeFormatter.ofPattern(
                                "HH:mm", Locale("ru")
                            )), fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp))
                        }
                        IconButton(onClick = {enabled2.value = !enabled2.value}) {
                            Icon(painterResource(R.drawable.clock), "",
                                Modifier.size(16.dp))
                        }
                    }
                }
            }
            HorizontalDivider()
            Row() {
                Column(Modifier.padding(start = 13.dp, top = 4.dp)) {
                    Text("Кабинет", fontSize = 12.sp,
                        color = Color(0xff636363),
                        fontWeight = FontWeight.SemiBold)
                    Text(cabinet.value, fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 6.dp))
                }
            }
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
                color = if (isFocused) Color(0xff155DFC).copy(alpha = 0.6f)
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
                                color = Color(0xff636363),
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



@Preview
@Composable
private fun DDD() {
    BookScreen()
}