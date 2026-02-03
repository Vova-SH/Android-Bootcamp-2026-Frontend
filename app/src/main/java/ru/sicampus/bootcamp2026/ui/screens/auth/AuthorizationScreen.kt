package ru.sicampus.bootcamp2026.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.components.UserField
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun AuthorizationScreen(
    viewModel: AuthViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
            .verticalScroll(scrollState))
    {
        Spacer(modifier = Modifier.height(100.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .padding(24.dp)) {
            Text(text = "С возвращением!",
                fontSize = 33.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                color = White
            )
        }

        Spacer(modifier = Modifier.height(100.dp))

        Card(shape = RoundedCornerShape(topStart = 30.dp,
            topEnd = 30.dp),
            colors = CardDefaults.cardColors(White),
            modifier = Modifier.fillMaxSize()

        ) {
            Spacer(modifier = Modifier.height(35.dp))

            when (val currentState = state) {
                is AuthState.Data -> Content(viewModel, currentState)
                is AuthState.Loading -> {
                    CircularProgressIndicator()
                }
            }

            Spacer(modifier = Modifier.height(150.dp))

        }

    }
}

@Composable
fun Content(
    viewModel: AuthViewModel,
    state: AuthState.Data
) {
    var login by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    val focusPasswordRequester = remember { FocusRequester() }
    var isEditable by remember { mutableStateOf(true) }

    UserField(
        "Email","Введите email", login, isEditable = isEditable,
        R.drawable.email
    )

    Spacer(modifier = Modifier.height(20.dp))

    UserField(
        "Пароль","Введите пароль", password, isEditable = isEditable,
        R.drawable.password
    )
    Text(
        text = "Забыли пароль",
        color = Blue,
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
        ,modifier = Modifier.padding(24.dp)
    )

    Spacer(modifier = Modifier.height(70.dp))
    Button(onClick = {},
        modifier = Modifier
            .height(63.dp)
            .width(270.dp)
            .clickable(
                onClick = {},
            ),

        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(Blue)
    ) {
        Text(text="Войти", color= White,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)))
        Icon(
            painter = painterResource(R.drawable.arrow),
            contentDescription = "Вход",
            tint = White,
            modifier = Modifier.padding(start=6.dp))
    }
}

// ПЕРЕДЕЛАТЬ UserField, навигация с экрана авторизации, регистрация