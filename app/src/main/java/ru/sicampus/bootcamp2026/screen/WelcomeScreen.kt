package ru.sicampus.bootcamp2026

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.screen.main.AuthScreen
import kotlin.jvm.java

@Composable
fun WelcomeScreen() {
    var isAutorized by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val intent = Intent(context, AuthScreen()::class.java)
    Column(modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Добро пожаловать в [название приложения]\n" +
                " Войдите или зарегистрируйтесь для начала работы",
            fontSize = 24.sp,
            fontWeight = FontWeight(800),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 69.dp))
        Spacer(modifier = Modifier.height(217.dp))
        Button(onClick = {context.startActivity(intent)}, shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Text("Войти", fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp))
        }
        Spacer(modifier = Modifier.height(7.dp))
        Button(onClick = {}) {
            Text("Зарегистрироваться", fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
        }
    }
}

@Preview
@Composable
fun ShowScreen() {
    WelcomeScreen()
}