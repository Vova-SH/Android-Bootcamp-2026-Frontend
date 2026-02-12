package ru.sicampus.bootcamp2026.android.ui.testScreens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.ui.components.CustomNavigationBar
import ru.sicampus.bootcamp2026.android.ui.components.CustomTextField
import ru.sicampus.bootcamp2026.android.ui.theme.Black
import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.android.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is ProfileAction.NavigateTo -> {
                    navController.navigate(action.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            CustomNavigationBar(
                navController = navController
            )
        }
    ) { paddingValues ->
        when (val currentState = state) {
            is ProfileState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProfileState.Data -> {
                ProfileContent(
                    state = currentState,
                    paddingValues = paddingValues,
                    onExitClick = { viewModel.onIntent(ProfileIntent.Logout) },
                    onSaveClick = { fullName, department, position ->
                        viewModel.onIntent(
                            ProfileIntent.UpdateProfile(
                                fullName = fullName,
                                department = department,
                                position = position
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileState.Data,
    paddingValues: PaddingValues,
    onExitClick: () -> Unit,
    onSaveClick: (String, String, String) -> Unit
)
{
    var fullName by remember(state.fullName) { mutableStateOf(state.fullName) }
    var department by remember(state.department) { mutableStateOf(state.department) }
    var position by remember(state.position) { mutableStateOf(state.position) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Spacer(modifier = Modifier.width(30.dp))

            IconButton(onClick = onExitClick) {
                Icon(
                    painter = painterResource(R.drawable.log_out_icon),
                    contentDescription = "Выйти",
                    tint = Black,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = state.fullName,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.open_sans_bold)),
            color = Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {

            // Отдел
            CustomTextField(
                value = department,
                onValueChange = { department = it },
                label = stringResource(R.string.department),
                modifier = Modifier,
                textColor = Black,
                enabled = state.isEditMode
            )

            // Должность
            CustomTextField(
                value = position,
                onValueChange = { position = it },
                label = stringResource(R.string.employee_position),
                modifier = Modifier,
                textColor = Black,
                enabled = state.isEditMode
            )

            // Почта (всегда read-only)
            CustomTextField(
                value = state.email,
                onValueChange = {},
                label = stringResource(R.string.email_text),
                modifier = Modifier,
                textColor = Black,
                enabled = false
            )

            // Пароль (только в режиме редактирования)
            CustomTextField(
                value = "••••••••",
                onValueChange = {},
                label = stringResource(R.string.password_text),
                textColor = Black,
                modifier = Modifier,
                enabled = false
            )
        }

        // Кнопка сохранения в режиме редактирования
        if (state.isEditMode) {
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    onSaveClick(
                        fullName,
                        department,
                        position
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 64.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    contentColor = White
                )
            ) {
                Text("Сохранить")
            }
        }

        // Показываем ошибку если есть
        if (state.error != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}