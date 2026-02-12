package ru.sicampus.bootcamp2026.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.components.ProfileField
import ru.sicampus.bootcamp2026.currentUser
import ru.sicampus.bootcamp2026.data.network.source.ImageLoaderViewModel
import ru.sicampus.bootcamp2026.selectedUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    isCurrentUser: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
) {
    viewModel.init(isCurrentUser)

    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is ProfileState.Content -> ProfileContentState(currentState, navController, modifier, isCurrentUser)
        is ProfileState.Error -> ProfileErrorState(currentState)
        is ProfileState.Loading -> ProfileLoadingState()
    }
}

@Composable
fun ProfileLoadingState() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun ProfileErrorState(
    state: ProfileState.Error,
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(
                onClick = state.onClickButton
            ) {
                Text(state.buttonText)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContentState(
    state: ProfileState.Content,
    navController: NavHostController,
    modifier: Modifier,
    isCurrentUser: Boolean
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val fraction = scrollBehavior.state.collapsedFraction

    var isEditable by remember { mutableStateOf(false) }
    (if (isCurrentUser) currentUser else selectedUser)?.let { user ->
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = user.surname + " " + user.name + " " + (user.patronymic ?: ""),
                            color = Color.White,
                            fontSize = lerp(24.sp, 20.sp, fraction)
                        )
                    },
                    navigationIcon = {
                        if (!isEditable) {
                            if (!isCurrentUser) {
                                IconButton({ navController.popBackStack() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        "Back",
                                        tint = Color.White
                                    )
                                }
                            }
                        } else {
                            IconButton({ isEditable = false }) {
                                Icon(Icons.Default.Close, "Отмена", tint = Color.White)
                            }
                        }
                    },
                    actions = {
                        Row {
                            if (!isEditable && isCurrentUser) {
                                IconButton({ isEditable = true }) {
                                    Icon(Icons.Filled.Edit, "Edit", tint = Color.White)
                                }
                                IconButton({ /* TODO */ }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ExitToApp,
                                        "Logout",
                                        tint = Color.White
                                    )
                                }
                            } else if (isEditable) {
                                IconButton({ /* TODO */ }) {
                                    Icon(Icons.Default.Check, "Сохранить", tint = Color.White)
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Blue,

                        ),
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding ->
            val imageLoaderViewModel: ImageLoaderViewModel = viewModel()
            LaunchedEffect(user.avatarUrl) {
                imageLoaderViewModel.loadImage(user.avatarUrl)
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                imageLoaderViewModel.userAvatar.value?.asImageBitmap()?.let { bitmap ->
                    Image(
                        bitmap,
                        "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .alpha(1f - fraction)
                    )
                }
                Column(
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                ) {
                    if (isEditable) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button({

                            }) {
                                Text("Нажмите, чтобы сменить аватар")
                            }
                        }
                    }
                    if (isEditable) {
                        var fio by remember {
                            mutableStateOf(
                                (user.surname + " " + user.name + " " + user.patronymic) ?: ""
                            )
                        }
                        TextField(
                            value = fio,
                            onValueChange = { fio = it },
                            label = {
                                Text("ФИО")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileField("Email", user.mail, isEditable)
                    Row(Modifier.fillMaxWidth(), Arrangement.Center) { Text("Контакты:") }
                    if (user.contacts.isEmpty()) {
                        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                            Text("Контакты не добавлены")
                        }
                    } else {
                        user.contacts.forEach { (label, values) ->
                            ProfileField(label, values, isEditable, true)
                        }
                    }
                    if (isEditable) {
                        Button(
                            {}
                        ) {
                            Text("Добавить контакт")
                        }
                    }
                }
            }
        }
    }
}

