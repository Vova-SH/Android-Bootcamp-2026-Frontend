package ru.sicampus.bootcamp2026.ui.screen.meeting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.components.CardEmployee
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.selectedMeeting
import ru.sicampus.bootcamp2026.ui.theme.Pink80
import ru.sicampus.bootcamp2026.ui.theme.Purple80

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Meeting(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val meetingData = selectedMeeting ?: return
    var isExpanded by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Информация о встрече",
                        color = Color.Black,
                        style = TextStyle(fontSize = 20.sp),
                        fontWeight = FontWeight.W800,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { /* TODO назад (это я тоже сам доделаю) */ },
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {navController.navigate("")},
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            modifier = Modifier.size(25.dp),
                            contentDescription = "Редактировать",
                            tint = Color.White
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
                modifier = Modifier
                    .width(366.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row {
                    Text(text = "Название:", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp))
                    Text(text = meetingData.name, color = Color.Black, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(text = "Выбранная дата:", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp))
                    Text(text = "" + meetingData.startBooking.dayOfMonth + "." + meetingData.startBooking.monthValue + "." + meetingData.startBooking.year, color = Color.Black, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(11.dp))
                Row {
                    Text(text = "Время начала:", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp))
                    Text(text = "" + meetingData.startBooking.hour + ":00", color = Color.Black, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Описание:",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .border(width = 1.dp, shape = RoundedCornerShape(48.dp), color = Color.Transparent)
                            .size(width = 32.dp, height = 40.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Pink80)
                    ) {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Развернуть", tint = Purple80)
                    }
                }

//                Text(
//                    text = meetingData.description,
//                    fontSize = 16.sp,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Участники",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
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
                    meetingData.invited.forEach {
                        val s = it.split(" ")
                        CardEmployee(
                            user = UserEntity(
                                surname = s[0],
                                name = s[1],
                                patronymic = s[2],
                                mail = s[3],
                            ),
//                            invitationState = state,
                            onCardClick = {
//                                selectedUser = user
//                                navController.navigate(NavRoutes.UserDetail.route)
                            }
                        )
                    }
                }
            }
        }
    }
}
