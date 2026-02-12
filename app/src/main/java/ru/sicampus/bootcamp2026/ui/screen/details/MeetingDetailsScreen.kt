package ru.sicampus.bootcamp2026.ui.screen.details


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.domain.home.entities.ParticipantEntity
import ru.sicampus.bootcamp2026.ui.screen.home.HomeState
import ru.sicampus.bootcamp2026.ui.screen.home.HomeViewModel
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography
import ru.sicampus.bootcamp2026.ui.theme.Green
import ru.sicampus.bootcamp2026.ui.theme.DarkGray1
import ru.sicampus.bootcamp2026.ui.theme.MediumGray
import ru.sicampus.bootcamp2026.ui.theme.Red
import ru.sicampus.bootcamp2026.ui.theme.SineyIney
import ru.sicampus.bootcamp2026.ui.theme.SoftWhite
import ru.sicampus.bootcamp2026.ui.theme.Yellow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun MeetingDetailScreen( viewModel : HomeViewModel, onReturnToHome: () -> Unit ) {
    //TODO сделать отдельным запросом
    val event = viewModel.getSelectedEvent()

    if (event == null) {
        LaunchedEffect(Unit) {
            onReturnToHome()
        }
        return
    }

    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is HomeState.Error -> DetailErrorState(currentState, onRefresh = { viewModel.getData() })
        is HomeState.Loading -> DetailLoadingState()
        is HomeState.Content -> DetailContentState(onReturnToHome, event)
    }

}

@Composable
private fun DetailLoadingState(){
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
private fun DetailErrorState( state: HomeState.Error, onRefresh: () -> Unit ){
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
private fun DetailContentState(onReturnToHome: () -> Unit , event: EventEntity){
    Column(
        modifier = Modifier.fillMaxSize().background(SoftWhite)
            .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
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
                text = "информация о встрече",
                color = Black,
                style = CustomTypography.displayMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(SineyIney)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoRow(label = "название", value = event.title, modifier = Modifier.padding(bottom = 8.dp))

                InfoRow(label = "описание", value = event.description, modifier = Modifier.padding(bottom = 16.dp))

                InfoRowDate(label = "дата", value = event.date, modifier = Modifier.padding(bottom = 8.dp))

                InfoRowTime(label = "время", value = event.startTime, modifier = Modifier.padding(bottom = 0.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "организатор",
                style = CustomTypography.labelMedium,
                color = Black.copy(alpha = 0.5f),
                modifier = Modifier.padding(10.dp, bottom = 12.dp)
            )

            // Плашка организатора
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .border(
                        width = 1.dp,
                        color = DarkGray1,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Аватарка
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
                        text = event.organizerName.split(" ").get(0) + " " + event.organizerName.split(" ").get(1),
                        style = CustomTypography.labelMedium,
                        color = Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Участники
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "участники",
                    style = CustomTypography.labelMedium,
                    color = Black.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = 10.dp)
                )

                Text(
                    text = "статус",
                    style = CustomTypography.labelMedium,
                    color = Black.copy(alpha = 0.5f),
                    modifier = Modifier.padding(end = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Список участников
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(event.participants) { participant ->
                    ParticipantCard(participant = participant)
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = CustomTypography.labelMedium,
            color = SoftWhite.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = value, style = CustomTypography.labelMedium, color = SoftWhite)
    }
}

@Composable
fun InfoRowTime(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = CustomTypography.labelMedium,
            color = SoftWhite.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(4.dp))
        val time = LocalTime.parse(value).format(DateTimeFormatter.ofPattern("HH:mm"))
        Text(text = time, style = CustomTypography.labelMedium, color = SoftWhite)
    }
}
@Composable
fun InfoRowDate(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = CustomTypography.labelMedium,
            color = SoftWhite.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(4.dp))
        val date = LocalDate.parse(value).format(DateTimeFormatter.ofPattern("dd-MM"))
        Text(text = date, style = CustomTypography.labelMedium, color = SoftWhite)
    }
}

@Composable
fun ParticipantCard(participant: ParticipantEntity) {
    val response = participant.status

    println("Статус: $response")

    val statusColor = when (participant.status) {
        "Принято" -> Green
        "Отказано" -> Red
        else -> {Yellow}
    }

    val statusText = participant.status

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(30.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFC8C8C8),
                shape = RoundedCornerShape(30.dp)
            )
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Левая часть: аватар и имя
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Аватарка
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFFE7E5E5))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Аватар пользователя",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Имя участника
                val nameAndSurname = participant.fullName.split(" ").get(0) + " " + participant.fullName.split(" ").get(1)
                Text(text = nameAndSurname, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Текст статуса
                Text(text = statusText, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.width(8.dp))
                // Кружок статуса
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MeetingDetailScreenPreview() {
//    Column( modifier = Modifier.fillMaxSize()
//    ) {
//        MeetingDetailScreen()
//    }
//}