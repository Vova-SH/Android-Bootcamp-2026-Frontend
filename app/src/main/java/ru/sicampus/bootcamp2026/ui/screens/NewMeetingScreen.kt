package ru.sicampus.bootcamp2026.ui.screens

import NewMeetingViewModel
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.theme.PrimaryPurple
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMeetingScreen(
    onBackClicked: () -> Unit,
    viewModel: NewMeetingViewModel = viewModel()
) {
    val users by viewModel.users.collectAsState()
    val selectedIds by viewModel.selectedIds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val uiEvent by viewModel.uiEvent.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf("11:00 - 12:00") }

    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        },
        selectedDate.year,
        selectedDate.monthValue - 1,
        selectedDate.dayOfMonth
    )

    LaunchedEffect(uiEvent) {
        uiEvent?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (it == "Встреча создана!") onBackClicked()
            viewModel.clearEvent()
        }
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Новая встреча", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundColor)
            )
        },
        bottomBar = {
            Box(Modifier.padding(16.dp)) {
                Button(
                    onClick = { viewModel.createMeeting(title, selectedDate, selectedTime, description) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Создать", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Тема", fontWeight = FontWeight.Bold)
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier.fillMaxWidth().height(56.dp).customDashedBorder(),
                decorationBox = { inner ->
                    Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
                        if (title.isEmpty()) Text("Тема встречи", color = Color.Gray)
                        inner()
                    }
                }
            )

            Text("Описание (опционально)", fontWeight = FontWeight.Bold)
            BasicTextField(
                value = description,
                onValueChange = { description = it },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier.fillMaxWidth().height(56.dp).customDashedBorder(),
                decorationBox = { inner ->
                    Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
                        if (description.isEmpty()) Text("Коротко о встрече", color = Color.Gray)
                        inner()
                    }
                }
            )

            Text("Дата", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .customDashedBorder()
                    .clickable { datePickerDialog.show() }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, EEEE", Locale("ru"))
                Text(
                    text = selectedDate.format(formatter),
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(Icons.Default.DateRange, null, modifier = Modifier.size(24.dp))
            }

            Text("Время", fontWeight = FontWeight.Bold)
            TimeSelector(selected = selectedTime, onSelect = { selectedTime = it })

            Text("Участники", fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = { Text("Кого позвать?") },
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )


            val listState = rememberLazyListState()
            val isAtBottom by remember {
                derivedStateOf {
                    val layoutInfo = listState.layoutInfo
                    val visibleItemsInfo = layoutInfo.visibleItemsInfo
                    if (layoutInfo.totalItemsCount == 0) false
                    else {
                        val last = visibleItemsInfo.last()
                        (last.index + 1 == layoutInfo.totalItemsCount)
                    }
                }
            }
            LaunchedEffect(isAtBottom) { if (isAtBottom) viewModel.loadNextPage() }

            Box(
                modifier = Modifier.weight(1f).customDashedBorder().clip(RoundedCornerShape(12.dp))
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(users) { user ->
                        UserRow(
                            name = user.name ?: "Без имени",
                            position = user.position ?: "Без должности",
                            isSelected = selectedIds.contains(user.id),
                            onToggle = { viewModel.toggleSelection(user.id) }
                        )
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Composable
fun UserRow(name: String, position: String, isSelected: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Medium)
            Text(position, fontSize = 12.sp, color = Color.Gray)
        }
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = PrimaryPurple)
        )
    }
}

@Composable
fun TimeSelector(selected: String, onSelect: (String) -> Unit) {
    val times = listOf(
        "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 13:00",
        "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00",
        "17:00 - 18:00", "18:00 - 19:00", "19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00"
    )

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.width(3.dp))

        times.forEach { time ->
            val isSelected = time == selected
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) PrimaryPurple else Color(0xFFF3F4F6))
                    .clickable { onSelect(time) }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = time,
                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}