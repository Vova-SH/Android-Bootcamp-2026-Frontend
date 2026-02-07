package ru.sicampus.bootcamp2026.ui.screen.profile

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.navigation.NavBar
import ru.sicampus.bootcamp2026.navigation.Routes
import ru.sicampus.bootcamp2026.components.ChangePFP
import ru.sicampus.bootcamp2026.components.EditableField
import ru.sicampus.bootcamp2026.components.ProfilePicture
import ru.sicampus.bootcamp2026.components.SimpleButton

import androidx.compose.material3.CircularProgressIndicator
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileStateModel = viewModel<ProfileStateModel>()
) {
    val state by viewModel.uiState.collectAsState()
    
    ProfileScreenContent(
        state = state,
        navController = navController,
        onRefresh = { viewModel.getData() },
        onNameChange = { viewModel.updateName(it) },
        onSave = { viewModel.saveProfile() }
    )
}

@Composable
private fun ProfileScreenContent(
    state: ProfileState,
    navController: NavHostController,
    onRefresh: () -> Unit,
    onNameChange: (String) -> Unit,
    onSave: () -> Unit
) {
    when (state) {
        is ProfileState.Loading -> ProfileStateLoading()
        is ProfileState.Error -> ProfileStateError(state, onRefresh = onRefresh)
        is ProfileState.Content -> ProfileStateContent(
            state = state,
            navController = navController,
            onNameChange = onNameChange,
            onSave = onSave
        )
    }
}

@Composable
private fun ProfileStateContent(
    state: ProfileState.Content,
    navController: NavHostController,
    onNameChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .imePadding()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    ProfilePicture(imageVector = Icons.Filled.Person)
                    ChangePFP()
                }

                Spacer(modifier = Modifier.height(30.dp))

                EditableField(
                    value = state.fullName,
                    onValueChange = onNameChange,
                    label = "ФИО",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(25.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        "Электронная почта",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        state.email,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                SimpleButton("Сохранить") {
                    onSave()
                }
            }

            IconButton(
                onClick = { 
                    navController.navigate(Routes.Auth.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Выход",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileStateError(
    state: ProfileState.Error,
    onRefresh: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Ошибка: ${state.reason}", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            SimpleButton("Повторить") {
                onRefresh()
            }
        }
    }
}

@Composable
private fun ProfileStateLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        ProfileScreenContent(
            state = ProfileState.Content(
                fullName = "Иван Иванов",
                email = "user1@test.ru"
            ),
            navController = rememberNavController(),
            onRefresh = {},
            onNameChange = {},
            onSave = {}
        )
    }
}