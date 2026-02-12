package ru.sicampus.bootcamp2026.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import ru.sicampus.bootcamp2026.ui.theme.Blue
//import ru.sicampus.bootcamp2026.ui.theme.Golyboi
//import ru.sicampus.bootcamp2026.ui.theme.Gray
//import ru.sicampus.bootcamp2026.ui.theme.LGray
//import ru.sicampus.bootcamp2026.ui.theme.LightGray
//import ru.sicampus.bootcamp2026.ui.theme.White
//import ru.sicampus.bootcamp2026.ui.theme.Golyboi
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RegisterScreen(
//    onRegisterClick: () -> Unit,
//    onLoginClick: () -> Unit
//) {
//    var login by remember { mutableStateOf("") }
//    var name by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Blue)
//            .padding(horizontal = 24.dp)
//            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(40.dp))
//
//        RegistrationField(
//            value = login,
//            onValueChange = { login = it },
//            label = "Логин",
//            placeholder = "Введите логин..."
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        RegistrationField(
//            value = name,
//            onValueChange = { name = it },
//            label = "Имя",
//            placeholder = "Введите имя..."
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        RegistrationField(
//            value = lastName,
//            onValueChange = { lastName = it },
//            label = "Фамилия",
//            placeholder = "Введите фамилию..."
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        RegistrationField(
//            value = phone,
//            onValueChange = { phone = it },
//            label = "Телефон",
//            placeholder = "Введите номер телефона..."
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        RegistrationField(
//            value = email,
//            onValueChange = { email = it },
//            label = "Почта",
//            placeholder = "Введите почту..."
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        RegistrationField(
//            value = password,
//            onValueChange = { password = it },
//            label = "Пароль",
//            placeholder = "Введите пароль..."
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        RegistrationField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            label = "Подтвердите пароль",
//            placeholder = "Введите пароль..."
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Button(
//            onClick = onRegisterClick,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            shape = RoundedCornerShape(15.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = LightGray
//            )
//        ) {
//            Text(
//                text = "Зарегистрируйтесь",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.ExtraBold
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Row (
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            Text(
//                text = "Есть аккаунт? ",
//                color = LightGray,
//                fontSize = 16.sp
//            )
//            TextButton(
//                onClick = onLoginClick
//            ) {
//                Text(
//                    text = "Войдите",
//                    color = Golyboi,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(40.dp))
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RegistrationField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    placeholder: String,
//    modifier: Modifier = Modifier,
//) {
//    Column(modifier = modifier) {
//        Text(
//            text = label,
//            fontSize = 20.sp,
//            color = White,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = {
//                Text(
//                    text = placeholder,
//                    color = Gray.copy(alpha = 0.5f),
//                    fontSize = 18.sp
//                )
//            },
//            shape = RoundedCornerShape(10.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Gray.copy(alpha = 0.5f),
//                unfocusedBorderColor = LGray,
//                focusedContainerColor = LightGray,
//                unfocusedContainerColor = LightGray
//            ),
//            singleLine = true
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Экран регистрации")
//@Composable
//fun ShowRegistrationScreen() {
//    MaterialTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            RegisterScreen(
//                onRegisterClick = {},
//                onLoginClick = {}
//            )
//        }
//    }
//}