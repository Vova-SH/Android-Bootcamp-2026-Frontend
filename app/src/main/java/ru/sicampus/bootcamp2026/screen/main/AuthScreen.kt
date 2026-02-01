package ru.sicampus.bootcamp2026.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AuthScreen() {
    Column(modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = "", onValueChange = {},
            label = { Text("Почта") },
            placeholder = { Text("mail@example.com") })
        TextField(
            value = "", onValueChange = {},
            label = { Text("Пароль") },
            placeholder = { Text("********") },
            trailingIcon = {}
        )
        Button(onClick = {}) {
            Text("Войти")
        }
    }
}

@Preview(device = "id:pixel_9_pro_xl")
@Composable
fun ShowAuth() {
    AuthScreen()
}