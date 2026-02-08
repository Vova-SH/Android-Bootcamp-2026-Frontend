package com.example.meet.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.meet.Screen
import com.example.meet.data.source.Network
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun SplashScreen(navController: NavHostController) {
    var statusMessage by remember { mutableStateOf("Проверяем соединение с сервером...") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {

            val serverOk = try {
                Network.pingServer()
            } catch (_: Exception) {
                false
            }


            var isLoggedIn = Network.isLoggedIn

            if (!isLoggedIn) {
                val sessionRestored = try {
                    Network.restoreSession()
                } catch (_: Exception) {
                    false
                }
                isLoggedIn = sessionRestored
            }

            statusMessage = if (serverOk) {
                if (isLoggedIn) "Авторизация выполнена" else "Сервер доступен"
            } else {
                "Авторизация выполнена!"
            }

            delay(1500)

            val destination = if (isLoggedIn) Screen.MainMeet.route else Screen.Login.route
            navController.navigate(destination) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Планировщик встреч",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}