package ru.sicampus.bootcamp2026.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NotificationScreen(

) {
    Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
    ){
        Text(text = "Notification Screen", color = Color.White)
    }
}