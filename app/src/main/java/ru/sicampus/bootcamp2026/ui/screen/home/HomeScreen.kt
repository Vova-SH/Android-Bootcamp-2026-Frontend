package ru.sicampus.bootcamp2026.ui.screen.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import kotlinx.coroutines.delay
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.ImageRepository
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.ui.camera.CameraViewModal
import ru.sicampus.bootcamp2026.ui.camera.CameraViewModalFactory
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.BlackIcon
import ru.sicampus.bootcamp2026.ui.theme.BluePrimary
import ru.sicampus.bootcamp2026.ui.theme.Grey
import ru.sicampus.bootcamp2026.ui.theme.Yellow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.Unit

@Composable
fun HomeScreen( viewModel : HomeViewModel  ,onDetailClick: () -> Unit) {
    val user = remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        user.value = AuthLocalDataSource.getCurrentUser()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.getData()
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

    val repository = remember { ImageRepository() }
    val viewModelCamera: CameraViewModal = viewModel(
        factory = CameraViewModalFactory(repository)
    )
    val user = remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        val currentUser = AuthLocalDataSource.getCurrentUser()
        user.value = currentUser

        currentUser?.id?.let { id ->
            viewModelCamera.setUserId(id)
            viewModelCamera.loadProfileImage()
        }
    }


    var refreshKey by remember { mutableStateOf(0) }
    val imageUrl by viewModelCamera.imageUrl.collectAsState()

    LaunchedEffect(imageUrl) {
        viewModelCamera.loadProfileImage()
    }

    val imageUrlWithTimestamp = remember(imageUrl, refreshKey) {
        imageUrl?.let { url ->
            if (url.contains("?")) {
                "$url&refresh=${refreshKey}_${System.currentTimeMillis()}"
            } else {
                "$url?refresh=${refreshKey}_${System.currentTimeMillis()}"
            }
        }
    }

    LaunchedEffect(viewModelCamera.isUploading.collectAsState().value, viewModelCamera.imageUrl.collectAsState().value) {
        if (!viewModelCamera.isUploading.value && viewModelCamera.imageUrl.value != null) {
            delay(400)
            refreshKey++
            Log.d("REFRESH", "Force refresh triggered, key: $refreshKey")
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding( top = 56.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // аватарка и приветствие и кнопка уведомлений
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Левая часть: аватарка и приветствие
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Аватарка
                    Box(
                        modifier = Modifier.size(86.dp).clip(CircleShape).background(BluePrimary)
                    ) {
                        if (imageUrlWithTimestamp!= null){
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrlWithTimestamp)
                                    .memoryCachePolicy(CachePolicy.DISABLED)
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .build(),
                                contentDescription = "Фото профиля",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.person),
                                error = painterResource(id = R.drawable.person)
                            )
                        }
                    }

                    val fullName = user.value?.fullName ?: "Ивана"
                    val name = fullName.split(" ").getOrNull(1).takeIf { !it.isNullOrEmpty() } ?: "Ивана"
                    Column(
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text( text = "привет,", style = MaterialTheme.typography.displayMedium,color = Black.copy(0.5f) )
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
                color = Black
            )

            Surface(
                modifier = Modifier.width(106.dp).height(48.dp),
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
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }

            val todaysEvents = state.events.filter { it.date == LocalDate.now().toString() }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(todaysEvents) { event ->
                    EventCard(event = event, onDetailClick, viewModel)
                }
            }

            if (!todaysEvents.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 16.dp)
                ) {
                    Text(
                        text = "Пока здесь нет встреч",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}


@Composable
fun EventCard(event: EventEntity, onDetailClick: () -> Unit, viewModel: HomeViewModel) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable {
                viewModel.selectEvent(event)
                viewModel.getData()
                onDetailClick()
            },
        colors = CardDefaults.cardColors(containerColor = BluePrimary),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = "Стрелка",
                tint = Color(0xFF000409),
                modifier = Modifier.size(20.dp).align(Alignment.TopEnd)
            )

            // Инфа карточки
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text( text = event.title, style = MaterialTheme.typography.bodyMedium )
                Text( text = LocalTime.parse(event.startTime).format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.bodyLarge )
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

