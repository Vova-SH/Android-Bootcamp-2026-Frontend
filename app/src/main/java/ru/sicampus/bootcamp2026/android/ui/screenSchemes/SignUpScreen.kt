package ru.sicampus.bootcamp2026.android.ui.screenSchemes
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import ru.sicampus.bootcamp2026.R
//import ru.sicampus.bootcamp2026.android.ui.components.CustomButton
//import ru.sicampus.bootcamp2026.android.ui.components.CustomTextField
//import ru.sicampus.bootcamp2026.android.ui.theme.AppTheme
//import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
//import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
//import ru.sicampus.bootcamp2026.android.ui.theme.White
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SignUpScreen(
//    onBackClick: () -> Unit = {},
//    onButtonClick: () -> Unit = {}
//) {
//
//    var email by remember { mutableStateOf("") }
//    var fullName by remember { mutableStateOf("") }
//    var department by remember { mutableStateOf("") }
//    var position by remember { mutableStateOf("") }
//    var createPassword by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//
//    Scaffold(
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                painter = painterResource(R.drawable.arrow_icon),
//                contentDescription = "Назад",
//                modifier = Modifier
//                    .align(Alignment.Start)
//                    .padding(top = 46.dp, start = 24.dp)
//            )
//
//            Spacer(modifier = Modifier.height(87.dp))
//
//            Text(
//                text = "Регистрация",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                fontFamily = FontFamily(Font(R.font.open_sans_bold))
//            )
//
//            Spacer(modifier = Modifier.height(38.dp))
//
//            // ФИО
//            CustomTextField(
//                value = fullName,
//                onValueChange = { fullName = it },
//                label = "ФИО",
//                TextGrey
//            )
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Отдел
//            CustomTextField(
//                value = department,
//                onValueChange = { department = it },
//                label = "Отдел",
//                TextGrey
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Должность
//            CustomTextField(
//                value = position,
//                onValueChange = { position = it },
//                label = "Должность",
//                TextGrey
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            // Электронная почта
//            CustomTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = "Электронная почта",
//                TextGrey
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Придумайте пароль
//            CustomTextField(
//                value = createPassword,
//                onValueChange = { createPassword = it },
//                label = "Придумайте пароль",
//                TextGrey
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Подтвердите пароль
//            CustomTextField(
//                value = confirmPassword,
//                onValueChange = { confirmPassword = it },
//                label = "Подтвердите пароль",
//                TextGrey
//            )
//
//            Spacer(modifier = Modifier.height(57.dp))
//
//            // Кнопка регистрации
//            CustomButton(
//                "Зарегистрироваться",
//                DarkBlue,
//                White,
//                onButtonClick,
//                Modifier
//                    .padding(start = 64.dp, end = 64.dp)
//                    .fillMaxWidth()
//                    .height(36.dp)
//                )
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}
//
//@Preview
//@Composable
//fun ShowLogScreen(){
//    AppTheme {
//        SignUpScreen()
//    }
//}