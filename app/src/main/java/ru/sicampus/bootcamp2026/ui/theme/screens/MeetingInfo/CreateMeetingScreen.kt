package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.data.source.MeetingCreateNetDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.InverseSurface
import ru.sicampus.bootcamp2026.ui.theme.LightBlue
import ru.sicampus.bootcamp2026.ui.theme.OpDeepBlue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.fontFamily
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState


@Composable
fun CreateMeetingScreen(
    userPreferences: UserPreferences,
    appViewModel: AppViewModel,
) {


    val viewModel: MeetingInfoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MeetingInfoViewModel(appViewModel) as T
            }
        }
    )
    val appState by appViewModel.appState.collectAsStateWithLifecycle()


    val data = UsersInfoDataSource()
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    val people = remember { mutableStateListOf<String>() }
    val selected =  remember { mutableStateListOf<Long>() }
    val dataSource = MeetingCreateNetDataSource()
    val scope = rememberCoroutineScope()
    val userid = dataSource._userId.collectAsState().value


    LaunchedEffect(Unit) {
        data.getUsers(0, 10000).onSuccess {
            it.content?.forEach { user ->
                people.add("${user.id}|${user.fullName} ${user.jobTitle}")
            }
        }
    }


    Column(
        modifier = Modifier
            .width(412.dp)
            .height(917.dp)
            .background(LightBlue)
            .padding(16.dp)
    ) {
        // Заголовок
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Blue)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Создание встречи",
                color = Surface,
                fontFamily = fontFamily,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Поля ввода
        Field("Название встречи", name) { name = it }
        Field("Дата: yyyy-MM-dd", date) { date = it }
        Field("Время: HH:mm", time) { time = it }


        Spacer(modifier = Modifier.height(16.dp))

        // Участники
        Text(
            text = "Выберите участников",
            fontFamily = fontFamily,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(people) { person ->
                val parts = person.split("|")
                val userId = parts[0].toLong()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (userId in selected) OpDeepBlue else Gray,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                        .clickable {
                            if (userId in selected) {
                                selected.remove(userId)
                            } else {
                                selected.add(userId)
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = person, fontFamily = fontFamily)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                if (userId in selected) Blue else Surface,
                                RoundedCornerShape(4.dp)
                            )
                            .border(1.dp, InverseSurface.copy(0.3f), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (userId in selected) {
                            Text(text = "", color = Surface, fontSize = 14.sp)
                        }
                    }
                }
            }
        }


        Button(
            onClick = {
                scope.launch {
                    val success = dataSource.createMeeting(
                        meeting = MeetinCreateDTO(
                            name,
                            dateTime = viewModel.convertToISO(date, time).toString(),
                            selected
                        )
                    )
                    if (success) {
                        appViewModel.NavigateTo(ViewModelState.TimeTable)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Blue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Сохранить", color = Surface, fontFamily = fontFamily)
        }
    }
}




@Composable
fun Field(label: String, value: String, onValueChange: (String) -> Unit) {
    Text(text = label, fontFamily = fontFamily, modifier = Modifier.padding(bottom = 4.dp))
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Gray, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        textStyle = TextStyle(fontFamily = fontFamily, fontSize = 16.sp),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(text = "Введите $label", fontFamily = fontFamily, color = InverseSurface.copy(0.5f))
            }
            innerTextField()
        }
    )
    Spacer(modifier = Modifier.height(12.dp))
}

