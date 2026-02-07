package ru.sicampus.bootcamp2026.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.ui.theme.BlackIcon
import ru.sicampus.bootcamp2026.ui.theme.BluePrimary
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography
import ru.sicampus.bootcamp2026.ui.theme.Grey
import ru.sicampus.bootcamp2026.ui.theme.Yellow
import java.time.LocalDate
import kotlin.Unit

@Composable
fun HomeScreen( viewModel : HomeViewModel = viewModel<HomeViewModel>() ,onDetailClick: () -> Unit) {
    val user = remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        user.value = AuthLocalDataSource.getCurrentUser()
    }

    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is HomeState.Error -> HomeErrorState(currentState, onRefresh = { viewModel.getData() })
        is HomeState.Loading -> HomeLoadingState()
        is HomeState.Content -> HomeContentState(currentState, user, onDetailClick, viewModel)
    }

}

@Composable
private  fun HomeLoadingState(){
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
private  fun HomeErrorState( state: HomeState.Error, onRefresh: () -> Unit ){
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
private fun HomeContentState(
    state: HomeState.Content,
    user: MutableState<UserDto?>,
    onDetailClick: () -> Unit,
    viewModel: HomeViewModel
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding( top = 40.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // аватарка и приветствие и кнопка уведомлений
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Левая часть: аватарка и приветствие
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Аватарка
                    Box(
                        modifier = Modifier.size(86.dp).clip(CircleShape).background(BluePrimary)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Аватар пользователя",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    val name = user.value?.fullName ?: "Ивана"
                    Column() {
                        Text( text = "привет,", style = MaterialTheme.typography.displaySmall )
                        Text( text = name, style = MaterialTheme.typography.displayMedium )
                    }
                }

                // уведомления
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(Grey)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "Уведомления",
                        tint = BlackIcon,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = "готовы к вызовам сегодняшнего дня?",
                style = MaterialTheme.typography.displayLarge,
                color = Color(0xFF666666)
            )

            Surface(
                modifier = Modifier.width(140.dp).height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = Yellow,
                tonalElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "сегодня",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.Black
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.events) { event ->
                    if (event.date == LocalDate.now().toString()){
                        EventCard(event = event, onDetailClick, viewModel)
                    }

                }
            }
        }

    }
}


@Composable
fun EventCard(event: EventEntity, onDetailClick: () -> Unit, viewModel: HomeViewModel) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = BluePrimary
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {

            IconButton(
                onClick = {
                    viewModel.selectEvent(event)
                    viewModel.getData()
                    onDetailClick()
                },
                modifier = Modifier.size(32.dp).align(Alignment.TopEnd)
            ){
                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = "Стрелка",
                    tint = Color(0xFF000409),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Инфа карточки
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text( text = event.title, style = MaterialTheme.typography.bodyMedium )
                Text( text = event.startTime, style = MaterialTheme.typography.bodyLarge )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    MaterialTheme(
//        typography = CustomTypography
//    ) {
//        HomeScreen()
//    }
//}

