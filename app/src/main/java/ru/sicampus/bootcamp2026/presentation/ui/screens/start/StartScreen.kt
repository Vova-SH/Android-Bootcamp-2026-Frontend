package ru.sicampus.bootcamp2026.presentation.ui.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.presentation.ui.navigation.routes.AuthRoute
import ru.sicampus.bootcamp2026.presentation.ui.navigation.routes.RegRoute

@Composable
fun StartScreen(nav: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {
            nav.navigate(AuthRoute)
        }) {
            Text("Войти")
        }

        Button(onClick = {
            nav.navigate(RegRoute)
        }) {
            Text("Зарегистрироваться")
        }
    }
}