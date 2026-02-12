package ru.sicampus.bootcamp2026.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.accentBlue

@Composable
fun Profile(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fullName = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("ФИО")},
            value = fullName.value,
            onValueChange = {it -> fullName.value = it},
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )
        val workEmail = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("email")},
            value = workEmail.value,
            onValueChange = {it -> workEmail.value = it},
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )
        val position = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("Должность")},
            value = position.value,
            onValueChange = {it -> position.value = it},
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text("Опасная зона ⚠️", fontSize = 25.sp,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.padding(vertical = 1.dp))
        HorizontalDivider(thickness = 2.dp, color = Color.Black, modifier = Modifier.clip(RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.padding(vertical = 1.dp))

        val pass = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("Пароль")},
            value = pass.value,
            onValueChange = {it -> pass.value = it},
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )
        val verificationPass = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("Повторите пароль")},
            value = verificationPass.value,
            onValueChange = {it -> verificationPass.value = it},
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Button(onClick = {},
            colors = ButtonDefaults.buttonColors(
                accentBlue
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ){
            Text("Сохранить данные")
        }
    }
}