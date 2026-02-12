package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.accentBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp (
    modifier: Modifier = Modifier,
    onNavigateToTimeTable: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*LargeTopAppBar(title = {Text("Регистрация")}, colors = TopAppBarDefaults.topAppBarColors(
            transporant
        ))*/
        Text("Регистрация", fontSize = 30.sp)
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        val login = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("Логин")},
            value = login.value,
            onValueChange = {it -> login.value = it},
            shape = RoundedCornerShape(20.dp)
        )
        val workEmail = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("email")},
            value = workEmail.value,
            onValueChange = {it -> workEmail.value = it},
            shape = RoundedCornerShape(20.dp)
        )
//        val personalEmail = remember{mutableStateOf("")}
//        OutlinedTextField(
//            label = {Text("Личный email")},
//            value = personalEmail.value,
//            onValueChange = {it -> personalEmail.value = it},
//            shape = RoundedCornerShape(20.dp)
//        )
        val position = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("Должность")},
            value = position.value,
            onValueChange = {it -> position.value = it},
            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        val pass = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("Пароль")},
            value = pass.value,
            onValueChange = {it -> pass.value = it},
            shape = RoundedCornerShape(20.dp)
        )
        val verificationPass = remember{mutableStateOf("")}
        OutlinedTextField(
            label = {Text("Повторите пароль")},
            value = verificationPass.value,
            onValueChange = {it -> verificationPass.value = it},
            shape = RoundedCornerShape(20.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Button(onClick = {onNavigateToTimeTable()},
            colors = ButtonDefaults.buttonColors(
                accentBlue
            ),
            modifier = Modifier.width(275.dp),
            shape = RoundedCornerShape(15.dp)
        ){
            Text("Регистрация")
        }
    }
}

/*@Preview
@Composable
fun see2() {
    SignUp()
    //NavigationBar() { }
}*/
