package ru.sicampus.bootcamp2026.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import ru.sicampus.bootcamp2026.presentation.screen.creating_meeting.CreatingMeetingScreen
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme(
                darkTheme = false
            ) {
                CreatingMeetingScreen(
                    onBackClick = {},
                    onFinished = {}
                )
            }
        }
    }
}