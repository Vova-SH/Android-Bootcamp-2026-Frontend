package ru.sicampus.bootcamp2026.ui.theme.screens.CreateNewMeeting

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingResponseScreen
import kotlin.collections.List


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateNewMeetingScreen(
    viewModel: CreateNewMeetingViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is CreateNewMeetingState.Error -> CreateNewMeetingErrorState(currentState, onRefresh = {viewModel.getData()})
        is CreateNewMeetingState.Loading -> CreateNewMeetingLoadingState()
        is CreateNewMeetingState.Content -> CreateNewMeetingContent(currentState)
    }
}

@Composable
private fun CreateNewMeetingLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun CreateNewMeetingErrorState(
    state: CreateNewMeetingState.Error,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(
                onClick = onRefresh
            ) {
                Text("refresh")
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun CreateNewMeetingContent(
    state: CreateNewMeetingState.Content
) { //пока что только архитектура, в дальнейшем перенаправлять и отображать данные из CreateNewMeetingState
    Scaffold() {
        Column (
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Surface,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(40.dp).fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.height(120.dp))

                        MeetingFields("Название", "Название встречи")
                        MeetingFields("Описание", "Очень длинное описание предстоящей встречи, которое придумал " +
                                "сотрудник, чтобы все поняли, для чего она нужна")
                        MeetingFields("Место", "Место встречи")
                        MeetingFields("Дата", "08.02.2026")
                        MeetingFields("Время", "18:00-19:00")
                        Text(
                            text = "Список участников",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                    }
                }
                Image(
                    painter = painterResource(R.drawable.meeting_wave2),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.meeting_wave1),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                IconButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.TopEnd).padding(7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.White,
                        contentDescription = "закрыть"
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(30.dp),
            ) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                ){
                    Text(
                        "Создать"
                    )
                }
            }
        }
    }
}


@Composable
fun MeetingFields(
    label: String,
    value: String
) {
    var text by remember { mutableStateOf(value) }
    if (label=="Описание")
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            value = text,
            textStyle = Typography.labelSmall,
            onValueChange = { text = it },
            label = { Text(label) }
        )
    else
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            value = text,
            textStyle = Typography.labelSmall,
            onValueChange = { text = it },
            maxLines = 1,
            label = { Text(label) }
        )
}

@Preview
@Composable
fun PreviewCreateNewMeetingScreen() {
    AndroidBootcamp2026FrontendTheme() {
        CreateNewMeetingContent(CreateNewMeetingState.Content("", "", "", "", listOf("Анна", "Борис", "Василий")))
    }
}