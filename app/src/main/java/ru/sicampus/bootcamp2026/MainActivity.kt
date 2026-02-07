package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.sicampus.bootcamp2026.ui.screens.AppNavigation
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidBootcamp2026FrontendTheme {
                AppNavigation()
            }
        }
    }
}
