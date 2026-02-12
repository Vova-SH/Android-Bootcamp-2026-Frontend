package ru.sicampus.bootcamp2026.ui.screens.timetable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.screens.signin.SignInIntent
import ru.sicampus.bootcamp2026.ui.screens.users.UsersScreen
import ru.sicampus.bootcamp2026.ui.theme.accentBlue

@Composable
fun AddInvite() {
    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 4.dp)
        .border(
        width = 2.dp,
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(16.dp)
    )) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text("Создать встречу", fontSize = 25.sp,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.padding(vertical = 1.dp))
            val title = remember{mutableStateOf("")}
            OutlinedTextField(
                label = {Text("Название встречи")},
                value = title.value,
                onValueChange = {it -> title.value = it},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            UsersScreen()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    accentBlue
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                //enabled = state.isEnabledSend
            ) {
                Text("Создать встечу")
            }
        }
    }
}