package ru.sicampus.bootcamp2026.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.theme.Primary

@Preview(showBackground = true)
@Composable
fun ProfileScreen() {
    var name by remember { mutableStateOf("АННа Петровна") }

    var phone by remember { mutableStateOf("7 9595 979565") }
    var email by remember { mutableStateOf("anna,ptryoto") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text =" мой профиль",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it},
            label = {Text("Имя")},
            leadingIcon = {Icon(Icons.Default.Person, contentDescription = "Имя")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it},
            label ={ Text("Телефон")},
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Телефон")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Почта")},
            leadingIcon = {Icon (Icons.Default.Email, contentDescription = "Почта")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { println("сохранено") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0057B8)
            )
        ) {
            Text("Сохранить изменения")
        }

        Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0057B8)
            )
        ) {
            Text("Новая встреча")
        }

        Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0057B8)
            )
        ) {
            Text("Календарь")
        }

    }
}

