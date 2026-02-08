package ru.sicampus.bootcamp2026

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.screen.meetings.CreateMeetingViewModel
import ru.sicampus.bootcamp2026.ui.theme.Pink80
import ru.sicampus.bootcamp2026.ui.theme.Purple80

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeeting(navController: NavHostController,
                  viewModel: CreateMeetingViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var isExpanded by remember { mutableStateOf(true) }
    if (state.success) {
        navController.popBackStack()
    }
//    val data = listOf(
//        UserData(
//            "Иванов",
//            "Иван",
//            "Иванович",
//            telephone = "+79111234567",
//            mail = "ivanov@example.com"
//        ) to MeetingData.InvitationState.Agree,
//        UserData("Петров", "Пётр", telephone = "+79223334455", mail = "petrov@example.com") to MeetingData.InvitationState.Disagree,
//        UserData("Сидорова", "Анна", telephone = "+79334445566") to MeetingData.InvitationState.NoAnswer,
//        UserData("Кузнецов", "Дмитрий") to MeetingData.InvitationState.Disagree,
//        UserData("Морозова", "Елена") to MeetingData.InvitationState.Disagree
//    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Создание встречи",
                        color = Color.Black,
                        style = TextStyle(fontSize = 24.sp),
                        fontWeight = FontWeight.W800,
                    )
                },
                navigationIcon = {
                    Button(onClick = {
                        navController.popBackStack()
                    }, modifier = Modifier.padding(horizontal = 5.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад")
                        Text(
                            "назад",
                            fontSize = 15.sp,
                        )
                    }
                },
                modifier = Modifier.padding(15.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text("Название") },
                    placeholder = { Text("Название этой встречи") },
                    isError = state.error != null,
                    trailingIcon = { Icon(Icons.Default.Cancel,
                        contentDescription = "Стереть") },
                    modifier = Modifier
                        .width(360.dp)
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .width(360.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        TextField(
                            value = state.selectedDate,
                            onValueChange = { viewModel.onDateChange(it) },
                            label = { Text("Дата") },
                            placeholder = { Text("2026-01-28") },
                            modifier = Modifier.width(360.dp).padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextField(
                            value = state.selectedTime,
                            onValueChange = { viewModel.onTimeChange(it) },
                            label = { Text("Время") },
                            placeholder = { Text("19:00") },
                            modifier = Modifier.width(360.dp).padding(horizontal = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.onCreateMeeting() },
                    enabled = state.name.isNotBlank() &&
                            state.selectedDate.isNotBlank() &&
                            state.selectedTime.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple80),
                    modifier = Modifier.width(360.dp).padding(horizontal = 16.dp)
                ) {
                    Text(
                        "Создать встречу",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Участники",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    IconButton(
                        onClick = {isExpanded = !isExpanded},
                        modifier = Modifier
                            .border(width = 1.dp, shape = RoundedCornerShape(48.dp), color = Color.Transparent)
                            .size(width = 32.dp, height = 40.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Pink80)
                    ) {
                        Icon(if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Свернуть" else "Развернуть",
                            tint = Purple80)
                    }
                }
            }
            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    data.forEach { (user, state) ->
//                        CardEmployee(
//                            user, state,
//                            {})
//                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ShowCreate() {
//    CreateMeeting(NavHostController())
}