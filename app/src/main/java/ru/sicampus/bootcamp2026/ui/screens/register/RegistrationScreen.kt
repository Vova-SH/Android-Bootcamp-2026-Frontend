package ru.sicampus.bootcamp2026.ui.screens.register

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.components.UserField
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun RegistrationScreen() {
    val scrollState = rememberScrollState()

    var isEditable by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
            .verticalScroll(scrollState))
    {
        Spacer(modifier = Modifier.height(100.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)) {
            Text(text = "Здравствуйте!",
                fontSize = 36.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                color = White
            )
            Text(text = "Создайте ваш аккаунт",
                fontSize = 20.sp,
                color = White,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                modifier = Modifier.padding(top=16.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        Card(shape = RoundedCornerShape(topStart = 30.dp,
                topEnd = 30.dp),
            colors = CardDefaults.cardColors(White),
            modifier = Modifier.fillMaxSize()

        ) {
            Spacer(modifier = Modifier.height(20.dp))

            UserField(
                "Email","Введите email", "", isEditable = isEditable,
                R.drawable.email
            )

            Spacer(modifier = Modifier.height(20.dp))

            UserField(
                "Пароль","Введите пароль", "", isEditable = isEditable,
                R.drawable.password
            )

            Spacer(modifier = Modifier.height(20.dp))

            UserField(
                "Организация","Введите организацию", "", isEditable = isEditable,
                R.drawable.company
            )


            Spacer(modifier = Modifier.height(20.dp))

            UserField(
                "ФИО","Введите ФИО", "", isEditable = isEditable,
                R.drawable.name
            )

            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {},
                modifier = Modifier
                    .height(63.dp)
                    .width(270.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(Blue),
            ) {
                Text(text="Зарегестрироваться", color= White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)))
                Icon(
                    painter = painterResource(R.drawable.arrow),
                    contentDescription = "Регистрация",
                    tint = White,
                    modifier = Modifier.padding(start=6.dp))
            }

            Spacer(modifier = Modifier.height(80.dp))

        }

    }
}

@Preview
@Composable
fun Show() {
    RegistrationScreen()
}
