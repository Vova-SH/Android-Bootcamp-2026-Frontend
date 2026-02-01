package ru.sicampus.bootcamp2026.presentation.ui.screens.main.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import ru.sicampus.bootcamp2026.presentation.ui.screens.main.meets.MeetsScreen
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun MeetCreateScreen() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        var name by rememberSaveable { mutableStateOf("") }
        var purpose by rememberSaveable { mutableStateOf("") }
        var date by rememberSaveable { mutableStateOf("") }

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Название встречи") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        TextField(
            value = purpose,
            onValueChange = { purpose = it },
            label = { Text("Цель") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text("Дата")
        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Дата") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row {
            Button(onClick = {}) {
                Text("Create")
            }
        }



    }
}

@Composable
fun InputField() {
    // todo общий компонент на все экраны в components папке
}

@Preview(showSystemUi = true)
@Composable
private fun MeetCreateScreenPreview() {
    AndroidBootcamp2026FrontendTheme() {
        MeetCreateScreen()
    }
}