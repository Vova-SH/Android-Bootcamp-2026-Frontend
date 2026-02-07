//package ru.sicampus.bootcamp2026.screens.Profile
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import coil3.compose.AsyncImage
//import ru.sicampus.bootcamp2026.domain.entities.UserEntity
//import ru.sicampus.bootcamp2026.ui.theme.*
//
//@Composable
//fun ProfileScreen(
//    navController: NavHostController,
//    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory())
//) {
//    ProfileScreenContent(
//        navController = navController,
//        viewModel = viewModel
//    )
//}
//
//@Composable
//fun ProfileScreenContent(
//    navController: NavHostController,
//    viewModel: ProfileViewModel
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    ProfileUI(
//        uiState = uiState,
//        onRefresh = { viewModel.refresh() },
//        onNavigateToCalendar = { navController.navigate("calendar") },
//        onNavigateToMeetings = { navController.navigate("meetings") },
//        onNavigateToSettings = { navController.navigate("settings") },
//        onLogout = {
//            navController.navigate("auth") {
//                popUpTo("profile") { inclusive = true }
//            }
//        }
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileUI(
//    uiState: ProfileUiState,
//    onRefresh: () -> Unit,
//    onNavigateToCalendar: () -> Unit,
//    onNavigateToMeetings: () -> Unit,
//    onNavigateToSettings: () -> Unit,
//    onLogout: () -> Unit
//) {
//    var selectedItem by remember { mutableStateOf(2) } // 2 = Профиль
//
//    Scaffold(
//        bottomBar = {
//            ProfileBottomNavigation(
//                selectedItem = selectedItem,
//                onItemSelected = { index ->
//                    selectedItem = index
//                    when (index) {
//                        0 -> onNavigateToCalendar()
//                        1 -> onNavigateToMeetings()
//                        2 -> Unit // Profile is already selected
//                        3 -> onNavigateToSettings()
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Blue)
//                .padding(paddingValues)
//        ) {
//            when (uiState) {
//                is ProfileUiState.Loading -> {
//                    LoadingView()
//                }
//
//                is ProfileUiState.Error -> {
//                    ErrorView(
//                        errorMessage = uiState.message,
//                        onRetry = onRefresh
//                    )
//                }
//
//                is ProfileUiState.Success -> {
//                    ProfileContent(
//                        user = uiState.user,
//                        onLogout = onLogout
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ProfileBottomNavigation(
//    selectedItem: Int,
//    onItemSelected: (Int) -> Unit
//) {
//    BottomAppBar(
//        containerColor = MaterialTheme.colorScheme.surface,
//        contentColor = MaterialTheme.colorScheme.onSurface,
//        modifier = Modifier.height(70.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            ProfileNavigationItem(
//                index = 0,
//                selectedIndex = selectedItem,
//                icon = "📅",
//                label = "Календарь",
//                onClick = { onItemSelected(0) }
//            )
//
//            ProfileNavigationItem(
//                index = 1,
//                selectedIndex = selectedItem,
//                icon = "👥",
//                label = "Встречи",
//                onClick = { onItemSelected(1) }
//            )
//
//            ProfileNavigationItem(
//                index = 2,
//                selectedIndex = selectedItem,
//                icon = "👤",
//                label = "Профиль",
//                onClick = { onItemSelected(2) }
//            )
//
//            ProfileNavigationItem(
//                index = 3,
//                selectedIndex = selectedItem,
//                icon = "⚙️",
//                label = "Настройки",
//                onClick = { onItemSelected(3) }
//            )
//        }
//    }
//}
//
//@Composable
//fun ProfileNavigationItem(
//    index: Int,
//    selectedIndex: Int,
//    icon: String,
//    label: String,
//    onClick: () -> Unit
//) {
//    val isSelected = index == selectedIndex
//    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.weight(1f)
//    ) {
//        TextButton(
//            onClick = onClick,
//            modifier = Modifier.height(50.dp)
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = icon,
//                    fontSize = 16.sp,
//                    color = textColor
//                )
//                Text(
//                    text = label,
//                    fontSize = 10.sp,
//                    color = textColor
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun LoadingView() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        CircularProgressIndicator(color = White)
//    }
//}
//
//@Composable
//fun ErrorView(
//    errorMessage: String,
//    onRetry: () -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = errorMessage,
//            color = MaterialTheme.colorScheme.error,
//            modifier = Modifier.padding(horizontal = 32.dp)
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = onRetry,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = LightGray,
//                contentColor = Blue
//            )
//        ) {
//            Text("Повторить")
//        }
//    }
//}
//
//@Composable
//fun ProfileContent(
//    user: UserEntity,
//    onLogout: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(vertical = 40.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if (user.photoUrl != null) {
//            AsyncImage(
//                model = user.photoUrl,
//                contentDescription = "Фото профиля",
//                modifier = Modifier
//                    .size(120.dp)
//                    .background(White, CircleShape)
//            )
//        } else {
//            Icon(
//                imageVector = Icons.Default.Person,
//                contentDescription = "Фото профиля",
//                modifier = Modifier
//                    .size(120.dp)
//                    .background(White, CircleShape)
//                    .padding(18.dp),
//                tint = LGray
//            )
//        }
//
//        Text(
//            text = if (user.photoUrl == null) "Добавить фото" else "Изменить фото",
//            color = White,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Medium,
//            modifier = Modifier.padding(top = 16.dp, bottom = 20.dp)
//        )
//
//        Column(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            ProfileInfoRow(label = "Имя", value = user.name)
//            ProfileInfoRow(label = "Фамилия", value = user.lastName)
//            ProfileInfoRow(label = "Email", value = user.email)
//            ProfileInfoRow(label = "Телефон", value = user.phoneNumber)
//            ProfileInfoRow(label = "Должность", value = user.position ?: "Не указана")
//            ProfileInfoRow(label = "Отдел", value = user.department ?: "Не указан")
//            ProfileInfoRow(label = "Логин", value = user.login)
//        }
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        // Кнопка выхода
//        Button(
//            onClick = onLogout,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = LightGray,
//                contentColor = Blue
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 32.dp)
//        ) {
//            Text("Выйти из аккаунта")
//        }
//    }
//}
//
//@Composable
//fun ProfileInfoRow(label: String, value: String) {
//    Column {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = label,
//                color = White,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium,
//                modifier = Modifier.padding(horizontal = 32.dp),
//            )
//
//            Text(
//                text = value,
//                fontSize = 16.sp,
//                color = LightGray,
//                modifier = Modifier.padding(horizontal = 32.dp),
//            )
//        }
//
//        Divider(
//            color = LightGray.copy(alpha = 0.3f),
//            thickness = 1.dp,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 32.dp)
//        )
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = false, name = "Профиль - Загрузка")
//@Composable
//fun ProfileLoadingPreview() {
//    MaterialTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            ProfileUI(
//                uiState = ProfileUiState.Loading,
//                onRefresh = {},
//                onNavigateToCalendar = {},
//                onNavigateToMeetings = {},
//                onNavigateToSettings = {},
//                onLogout = {}
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = false, name = "Профиль - Ошибка")
//@Composable
//fun ProfileErrorPreview() {
//    MaterialTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            ProfileUI(
//                uiState = ProfileUiState.Error("Не удалось загрузить данные"),
//                onRefresh = {},
//                onNavigateToCalendar = {},
//                onNavigateToMeetings = {},
//                onNavigateToSettings = {},
//                onLogout = {}
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = false, name = "Профиль - Успех")
//@Composable
//fun ProfileSuccessPreview() {
//    MaterialTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            ProfileUI(
//                uiState = ProfileUiState.Success(
//                    UserEntity(
//                        id = 1,
//                        name = "Амаль",
//                        lastName = "Шенкао",
//                        email = "nt@gmail.com",
//                        login = "amal",
//                        phoneNumber = "+79999999999",
//                        position = "Посудомойщик и водила",
//                        department = "Сотрудник",
//                        photoUrl = null
//                    )
//                ),
//                onRefresh = {},
//                onNavigateToCalendar = {},
//                onNavigateToMeetings = {},
//                onNavigateToSettings = {},
//                onLogout = {}
//            )
//        }
//    }
//}