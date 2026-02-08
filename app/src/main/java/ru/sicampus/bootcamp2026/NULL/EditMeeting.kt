//package ru.sicampus.bootcamp2026.ui.screen.meeting
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.Cancel
//import androidx.compose.material.icons.filled.KeyboardArrowDown
//import androidx.compose.material.icons.filled.KeyboardArrowUp
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.IconButtonDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import ru.sicampus.bootcamp2026.components.CardEmployee
//import ru.sicampus.bootcamp2026.selectedMeeting
//import ru.sicampus.bootcamp2026.ui.theme.Pink80
//import ru.sicampus.bootcamp2026.ui.theme.Purple80
//
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditMeeting() {
//    val scrollState = rememberScrollState()
//    val meetingData = selectedMeeting ?: return
//    var isExpanded by remember { mutableStateOf(true) }
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Изменить встречу",
//                        color = Color.Black,
//                        style = TextStyle(fontSize = 20.sp),
//                        fontWeight = FontWeight.W800,
//                        maxLines = 1
//                    )
//                },
//                navigationIcon = {
//                    IconButton(
//                        onClick = {},
//                        modifier = Modifier.padding(horizontal = 5.dp),
//                        colors = IconButtonDefaults.iconButtonColors(Purple80)
//                    ) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
//                    }
//                },
//                modifier = Modifier.padding(15.dp),
//                actions = {
//                    Button(
//                        onClick = {},
//                        modifier = Modifier.width(100.dp).height(32.dp),
//                        contentPadding = PaddingValues(horizontal = 4.dp)
//                    ) {
//                        Text("Сохранить", fontSize = 13.sp, maxLines = 1)
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .verticalScroll(scrollState)
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                TextField(
//                    value = "",
//                    onValueChange = {},
//                    label = { Text("Название") },
//                    placeholder = { meetingData.name },
//                    trailingIcon = { Icon(Icons.Default.Cancel,
//                        contentDescription = "Стереть") },
//                    modifier = Modifier
//                        .width(360.dp)
//                        .padding(horizontal = 16.dp)
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//                Row(
//                    modifier = Modifier
//                        .width(360.dp)
//                        .padding(horizontal = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Column {
//                        Row {
//                            Text(text = "Выбранная дата:", color = Color.Gray, fontSize = 16.sp,
//                                modifier = Modifier.padding(end = 6.dp))
//                            Text(text =  "" + meetingData.time.dayOfMonth + "." + meetingData.time.monthValue + "." + meetingData.time.year, color = Color.Black, fontSize = 16.sp)
//                        }
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Row {
//                            Text(text = "Выбранное время:", color = Color.Gray, fontSize = 16.sp,
//                                modifier = Modifier.padding(end = 6.dp))
//                            Text(text = "" + meetingData.time.hour + ":00", color = Color.Black, fontSize = 16.sp)
//                        }
//                    }
//                    Button(
//                        onClick = {},
//                        modifier = Modifier.height(32.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Purple80),
//                        contentPadding = PaddingValues(horizontal = 8.dp)
//                    ) {
//                        Text(
//                            text = "Изменить",
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.W500,
//                            color = Color.White
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(14.dp))
//
//                TextField(
//                    value = "",
//                    onValueChange = {},
//                    label = { Text("Описание") },
//                    placeholder = { meetingData.description },
//                    modifier = Modifier
//                        .width(360.dp)
//                        .height(200.dp)
//                        .padding(horizontal = 16.dp),
//                    trailingIcon = {
//                        Icon(Icons.Default.Cancel, contentDescription = "Стереть",
//                            modifier = Modifier.padding(bottom = 150.dp))
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Row(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Участники",
//                        color = Color.Black,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.W500
//                    )
//                    IconButton(
//                        onClick = {isExpanded = !isExpanded},
//                        modifier = Modifier
//                            .border(width = 1.dp, shape = RoundedCornerShape(48.dp), color = Color.Transparent)
//                            .size(width = 32.dp, height = 40.dp),
//                        shape = RoundedCornerShape(24.dp),
//                        colors = IconButtonDefaults.iconButtonColors(containerColor = Pink80)
//                    ) {
//                        Icon(if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
//                            contentDescription = if (isExpanded) "Свернуть" else "Развернуть",
//                            tint = Purple80)
//                    }
//                }
//            }
//            if (isExpanded) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    meetingData.users.forEach { (user, state) ->
//                        CardEmployee(
//                            user = user,
//                            invitationState = state,
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview
//@Composable
//fun ShowEditMeeting() {
//    EditMeeting()
//}