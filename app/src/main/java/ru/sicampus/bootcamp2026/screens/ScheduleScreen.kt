package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScheduleScreen() {
    Text("Расписание", modifier = Modifier.fillMaxSize().wrapContentHeight())
}