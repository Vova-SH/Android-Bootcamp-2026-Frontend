package ru.sicampus.bootcamp2026.ui.screen.mymeetings

import androidx.compose.foundation.lazy.items
import ru.sicampus.bootcamp2026.ui.screen.list.ListIntent
import ru.sicampus.bootcamp2026.ui.screen.list.ListState
import ru.sicampus.bootcamp2026.ui.screen.list.ListViewModel


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.ui.screen.home.HomeViewModel
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography
import ru.sicampus.bootcamp2026.ui.theme.Green
import ru.sicampus.bootcamp2026.ui.theme.MediumGray
import ru.sicampus.bootcamp2026.ui.theme.Red
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MyMeetingsScreen(
    viewModel : MyMeetingViewModel = viewModel<MyMeetingViewModel>(),
    onDetailClick: () -> Unit,
    homeViewModel: HomeViewModel,
    onReturnToHome: () -> Unit
) {

    val user = remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        user.value = AuthLocalDataSource.getCurrentUser()
    }

    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is MyMeetingsState.Error -> MeetingErrorState(currentState, onRefresh = { viewModel.getData() })
        is MyMeetingsState.Loading -> MeetingLoadingState()
        is MyMeetingsState.Content -> MeetingContentState(
            viewModel, homeViewModel, currentState, user, onDetailClick, onReturnToHome)
    }

}

@Composable
private  fun MeetingLoadingState(){
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
private  fun MeetingErrorState( state: MyMeetingsState.Error, onRefresh: () -> Unit ){
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

@Composable
private fun MeetingContentState(
    viewModel: MyMeetingViewModel,
    homeViewModel: HomeViewModel,
    state: MyMeetingsState.Content,
    user: MutableState<UserDto?>,
    onDetailClick: () -> Unit,
    onReturnToHome: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically
        ) {
            IconButton(
                onClick = { onReturnToHome() },
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
                text = "созданные Вами встречи",
                color = Black,
                style = CustomTypography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.meetings) { item ->
                InvitationCard(viewModel, homeViewModel,item, user, onDetailClick )
            }
        }
    }
}

@Composable
fun InvitationCard(viewModel: MyMeetingViewModel, homeViewModel: HomeViewModel, item: EventEntity , user: MutableState<UserDto?>, onDetailClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
            .padding(
                horizontal = 10.dp,
                vertical = 16.dp
            )
            .clickable {
                homeViewModel.selectEvent(item)
                onDetailClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            // Дата/Время + Название/Описание
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = LocalDate.parse(item.date).format(DateTimeFormatter.ofPattern("dd-MM")),
                        style = CustomTypography.bodySmall,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                    Text(
                        text = LocalTime.parse(item.startTime).format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = CustomTypography.bodySmall,
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.title,
                        style = CustomTypography.labelMedium,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = item.description,
                        style = CustomTypography.bodySmall,
                        color = Color.Black.copy(alpha = 0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp, end = 12.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .border(
                            width = 1.dp,
                            color = Red,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clickable {
                            val id = user.value?.id ?: 0
                            viewModel.onIntent(MyMeetingIntent.Send(item.id, id, false))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = "Отклонить",
                        tint = Red,
                        modifier = Modifier.size(24.dp).padding(2.dp)
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
//@Composable
//fun ListScreenPreview() {
//    MaterialTheme(
//        typography = CustomTypography
//    ) {
//        MyMeetingsScreen()
//    }
//}